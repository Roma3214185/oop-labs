package ua.medicine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.medicine.comparator.DrugComparator;
import ua.medicine.exception.XmlProcessingException;
import ua.medicine.model.Drug;
import ua.medicine.parser.DrugParser;
import ua.medicine.parser.ParserType;
import ua.medicine.transform.XslTransformer;
import ua.medicine.validator.ValidationResult;
import ua.medicine.validator.XmlValidator;

/**
 * Entry point: validates the medicines document, parses it with SAX,
 * StAX and DOM, sorts the drugs and groups them with XSLT.
 */
public class MedicineApplication {

    private static final Logger LOGGER =
            LogManager.getLogger(MedicineApplication.class);

    private final XmlValidator validator;
    private final XslTransformer transformer;
    private final Function<ParserType, DrugParser> parserProvider;

    /**
     * Creates the application with the given collaborators.
     *
     * @param validator      XML validator
     * @param transformer    XSL transformer
     * @param parserProvider creates parsers by type
     */
    public MedicineApplication(XmlValidator validator,
            XslTransformer transformer,
            Function<ParserType, DrugParser> parserProvider) {
        this.validator = validator;
        this.transformer = transformer;
        this.parserProvider = parserProvider;
    }

    /**
     * Runs the whole scenario.
     *
     * @param paths files to work with
     * @return true if every step succeeded
     */
    public boolean run(ApplicationPaths paths) {
        try {
            ValidationResult validation =
                    validator.validate(paths.xml(), paths.xsd());
            if (!validation.isValid()) {
                LOGGER.error("Document {} is invalid, stopping",
                        paths.xml());
                return false;
            }
            Map<ParserType, List<Drug>> parsed = parseWithAll(paths);
            List<Drug> drugs = parsed.get(ParserType.DOM);
            if (parsed.values().stream().anyMatch(d -> !d.equals(drugs))) {
                LOGGER.error("Parsers returned different results");
                return false;
            }
            LOGGER.info("All parsers returned the same {} drugs",
                    drugs.size());
            for (DrugComparator comparator : DrugComparator.values()) {
                logSorted(drugs, comparator);
            }
            transformer.transform(paths.xml(), paths.xsl(), paths.output());
            return true;
        } catch (XmlProcessingException e) {
            LOGGER.error("Processing failed: {}", e.getMessage(), e);
            return false;
        }
    }

    private Map<ParserType, List<Drug>> parseWithAll(ApplicationPaths paths)
            throws XmlProcessingException {
        Map<ParserType, List<Drug>> result = new EnumMap<>(ParserType.class);
        for (ParserType type : ParserType.values()) {
            result.put(type, parserProvider.apply(type).parse(paths.xml()));
        }
        return result;
    }

    /**
     * Sorts a copy of the drugs and logs the order.
     *
     * @param drugs      drugs to sort
     * @param comparator sort order
     * @return sorted copy
     */
    List<Drug> logSorted(List<Drug> drugs, DrugComparator comparator) {
        List<Drug> sorted = new ArrayList<>(drugs);
        sorted.sort(comparator);
        LOGGER.info("Sorted {}:", comparator);
        for (Drug drug : sorted) {
            LOGGER.info("  {} {} [{}] min price {}", drug.getId(),
                    drug.getName(), drug.getGroup().value(),
                    drug.getMinPrice().map(String::valueOf).orElse("-"));
        }
        return sorted;
    }

    /**
     * Starts the application.
     *
     * @param args nothing or {@code xml xsd xsl output}
     */
    public static void main(String[] args) {
        MedicineApplication application = new MedicineApplication(
                new XmlValidator(), new XslTransformer(),
                ParserType::createParser);
        boolean success = application.run(ApplicationPaths.fromArgs(args));
        LOGGER.info("Finished {}", success ? "successfully" : "with errors");
    }
}
