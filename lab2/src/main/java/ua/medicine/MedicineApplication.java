package ua.medicine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.medicine.comparator.DrugGroupComparator;
import ua.medicine.comparator.DrugNameComparator;
import ua.medicine.comparator.DrugPriceComparator;
import ua.medicine.model.Drug;
import ua.medicine.parser.DomDrugParser;
import ua.medicine.parser.DrugParser;
import ua.medicine.parser.SaxDrugParser;
import ua.medicine.parser.StaxDrugParser;
import ua.medicine.transformer.XslTransformer;
import ua.medicine.validator.XmlValidator;

/**
 * Точка входу: валідує XML, читає його трьома парсерами, сортує
 * препарати й групує їх через XSL. Усі повідомлення йдуть у лог.
 */
public class MedicineApplication {

    private static final Logger LOGGER =
            LogManager.getLogger(MedicineApplication.class);

    static final String XML = "/xml/medicines.xml";
    static final String XSD = "/xml/medicines.xsd";
    static final String XSL = "/xml/medicines-by-group.xsl";
    static final Path OUTPUT = Path.of("output", "medicines-by-group.xml");

    private final String xml;
    private final Path output;
    private final List<DrugParser> parsers;

    /**
     * Створює програму зі стандартними файлами й трьома парсерами.
     */
    public MedicineApplication() {
        this(XML, OUTPUT, List.of(new DomDrugParser(), new SaxDrugParser(),
                new StaxDrugParser()));
    }

    /**
     * Створює програму з іншими файлами чи парсерами (для тестів).
     *
     * @param xml     шлях до XML у ресурсах
     * @param output  куди записати результат XSL
     * @param parsers парсери, якими читати XML
     */
    MedicineApplication(String xml, Path output, List<DrugParser> parsers) {
        this.xml = xml;
        this.output = output;
        this.parsers = parsers;
    }

    /**
     * Виконує всі кроки по черзі.
     *
     * @return true, якщо всі кроки пройшли успішно
     */
    public boolean run() {
        try {
            byte[] document = readResource(xml);

            XmlValidator validator = new XmlValidator(open(readResource(XSD)));
            if (!validator.isValid(open(document))) {
                LOGGER.error("{} не відповідає схемі, далі не йдемо", xml);
                return false;
            }

            List<List<Drug>> results = new ArrayList<>();
            for (DrugParser parser : parsers) {
                results.add(parser.parse(open(document)));
            }
            List<Drug> drugs = results.get(0);
            if (!results.stream().allMatch(drugs::equals)) {
                LOGGER.error("Результати парсерів не збігаються");
                return false;
            }
            LOGGER.info("Усі {} парсери повернули однакові {} препаратів",
                    parsers.size(), drugs.size());

            logSorted("Сортування за назвою", drugs, new DrugNameComparator());
            logSorted("Сортування за групою", drugs, new DrugGroupComparator());
            logSorted("Сортування за найнижчою ціною", drugs,
                    new DrugPriceComparator());

            new XslTransformer().transform(open(document),
                    open(readResource(XSL)), output);
            return true;
        } catch (XmlException | IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    private static void logSorted(String title, List<Drug> drugs,
            Comparator<Drug> comparator) {
        List<Drug> sorted = new ArrayList<>(drugs);
        sorted.sort(comparator);
        LOGGER.info("{}:", title);
        for (Drug drug : sorted) {
            LOGGER.info(String.format(Locale.ROOT,
                    "  %-4s %-22s %-14s від %.2f грн", drug.getId(),
                    drug.getName(), drug.getGroup(), drug.getMinPrice()));
        }
    }

    private static byte[] readResource(String name)
            throws XmlException, IOException {
        try (InputStream in =
                MedicineApplication.class.getResourceAsStream(name)) {
            if (in == null) {
                throw new XmlException("Не знайдено файл " + name);
            }
            return in.readAllBytes();
        }
    }

    private static InputStream open(byte[] content) {
        return new ByteArrayInputStream(content);
    }

    /**
     * Запускає програму.
     *
     * @param args не використовуються
     */
    public static void main(String[] args) {
        boolean success = new MedicineApplication().run();
        LOGGER.info(success ? "Готово" : "Завершено з помилками");
    }
}
