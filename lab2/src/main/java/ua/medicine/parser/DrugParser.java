package ua.medicine.parser;

import java.nio.file.Path;
import java.util.List;

import ua.medicine.exception.XmlProcessingException;
import ua.medicine.model.Drug;

/**
 * Parses the medicines XML document into a collection of drugs.
 */
public interface DrugParser {

    /**
     * Parses the document.
     *
     * @param xml path to the XML document
     * @return drugs in document order
     * @throws XmlProcessingException if the document cannot be parsed
     */
    List<Drug> parse(Path xml) throws XmlProcessingException;
}
