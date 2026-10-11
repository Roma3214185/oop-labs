package ua.medicine.parser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.medicine.exception.XmlProcessingException;
import ua.medicine.model.Drug;

/**
 * Template of a parser: opens the document, delegates the parsing of
 * the stream to a subclass and handles logging and errors uniformly.
 */
public abstract class AbstractDrugParser implements DrugParser {

    private static final Logger LOGGER =
            LogManager.getLogger(AbstractDrugParser.class);

    @Override
    public final List<Drug> parse(Path xml) throws XmlProcessingException {
        String parserName = getClass().getSimpleName();
        LOGGER.debug("{} started parsing {}", parserName, xml);
        DrugAssembler assembler = new DrugAssembler();
        try (InputStream in =
                new BufferedInputStream(Files.newInputStream(xml))) {
            parse(in, assembler);
        } catch (XmlProcessingException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("{} failed to parse {}: {}", parserName, xml,
                    e.getMessage());
            throw new XmlProcessingException(
                    "Cannot parse " + xml + ": " + e.getMessage(), e);
        }
        List<Drug> drugs = assembler.getDrugs();
        LOGGER.info("{} parsed {} drugs from {}", parserName, drugs.size(),
                xml);
        return drugs;
    }

    /**
     * Reads the document and reports its elements to the assembler.
     *
     * @param in        document stream
     * @param assembler receiver of element events
     * @throws Exception if the document cannot be read
     */
    protected abstract void parse(InputStream in, DrugAssembler assembler)
            throws Exception;
}
