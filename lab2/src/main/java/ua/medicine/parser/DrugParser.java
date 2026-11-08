package ua.medicine.parser;

import java.io.InputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.medicine.XmlException;
import ua.medicine.model.Drug;

/**
 * Базовий клас парсерів XML-документа з препаратами.
 *
 * <p>Спільна частина (створити {@link DrugBuilder}, обробити помилки,
 * записати в лог) написана тут один раз. Нащадки реалізують лише
 * {@link #read}, тобто як саме пройти по документу.
 */
public abstract class DrugParser {

    private static final Logger LOGGER = LogManager.getLogger(DrugParser.class);

    /**
     * Читає препарати з XML-документа.
     *
     * @param xml вміст XML-документа
     * @return список препаратів у порядку з документа
     * @throws XmlException якщо документ не вдалося прочитати
     */
    public final List<Drug> parse(InputStream xml) throws XmlException {
        DrugBuilder builder = new DrugBuilder();
        try {
            read(xml, builder);
        } catch (Exception e) {
            throw new XmlException(
                    getName() + " не зміг прочитати XML: " + e.getMessage(), e);
        }
        List<Drug> drugs = builder.getDrugs();
        LOGGER.info("{} прочитав {} препаратів", getName(), drugs.size());
        for (Drug drug : drugs) {
            LOGGER.debug("  {}", drug);
        }
        return drugs;
    }

    /**
     * Повертає назву парсера для логів.
     *
     * @return назва класу
     */
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Проходить по документу й передає кожен тег у builder.
     *
     * @param xml     вміст XML-документа
     * @param builder куди передавати теги
     * @throws Exception будь-яка помилка читання
     */
    protected abstract void read(InputStream xml, DrugBuilder builder)
            throws Exception;
}
