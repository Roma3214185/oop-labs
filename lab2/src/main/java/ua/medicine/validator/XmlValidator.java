package ua.medicine.validator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ua.medicine.exception.XmlProcessingException;

/**
 * Validates XML documents against an XSD schema.
 */
public class XmlValidator {

    private static final Logger LOGGER =
            LogManager.getLogger(XmlValidator.class);

    /**
     * Validates the document and collects all problems.
     *
     * @param xml path to the XML document
     * @param xsd path to the XSD schema
     * @return validation result with all errors found
     * @throws XmlProcessingException if the schema or the document
     *                                cannot be read
     */
    public ValidationResult validate(Path xml, Path xsd)
            throws XmlProcessingException {
        LOGGER.debug("Validating {} against {}", xml, xsd);
        Validator validator = loadSchema(xsd).newValidator();
        CollectingErrorHandler handler = new CollectingErrorHandler();
        try {
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.setErrorHandler(handler);
            validator.validate(new StreamSource(xml.toFile()));
        } catch (SAXParseException e) {
            // fatal error: already recorded by the handler
            LOGGER.debug("Validation stopped: {}", e.getMessage());
        } catch (SAXException | IOException e) {
            throw new XmlProcessingException(
                    "Cannot validate " + xml + ": " + e.getMessage(), e);
        }
        ValidationResult result = new ValidationResult(handler.errors);
        if (result.isValid()) {
            LOGGER.info("{} is valid against {}", xml, xsd);
        } else {
            LOGGER.warn("{} is not valid: {} error(s)", xml,
                    result.errors().size());
            result.errors().forEach(error -> LOGGER.warn("  {}", error));
        }
        return result;
    }

    private static Schema loadSchema(Path xsd)
            throws XmlProcessingException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return factory.newSchema(xsd.toFile());
        } catch (SAXException e) {
            LOGGER.error("Cannot load schema {}: {}", xsd, e.getMessage());
            throw new XmlProcessingException(
                    "Cannot load schema " + xsd + ": " + e.getMessage(), e);
        }
    }

    /**
     * Stores every validation problem with its position.
     */
    private static final class CollectingErrorHandler
            implements ErrorHandler {

        private final List<String> errors = new ArrayList<>();

        @Override
        public void warning(SAXParseException e) {
            LOGGER.debug("Validation warning: {}", describe(e));
        }

        @Override
        public void error(SAXParseException e) {
            errors.add(describe(e));
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            errors.add(describe(e));
            throw e;
        }

        private static String describe(SAXParseException e) {
            return "line " + e.getLineNumber() + ", column "
                    + e.getColumnNumber() + ": " + e.getMessage();
        }
    }
}
