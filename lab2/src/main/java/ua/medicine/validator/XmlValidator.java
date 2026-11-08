package ua.medicine.validator;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ua.medicine.XmlException;

/**
 * Перевіряє XML-документ на відповідність XSD-схемі.
 */
public class XmlValidator {

    private static final Logger LOGGER =
            LogManager.getLogger(XmlValidator.class);

    private final Schema schema;

    /**
     * Завантажує схему один раз.
     *
     * @param xsd вміст XSD-схеми
     * @throws XmlException якщо схема некоректна
     */
    public XmlValidator(InputStream xsd) throws XmlException {
        try {
            schema = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(new StreamSource(xsd));
        } catch (SAXException e) {
            throw new XmlException(
                    "Не вдалося завантажити XSD: " + e.getMessage(), e);
        }
    }

    /**
     * Перевіряє документ. Першу знайдену помилку записує в лог.
     *
     * @param xml вміст XML-документа
     * @return true, якщо документ відповідає схемі
     */
    public boolean isValid(InputStream xml) {
        try {
            schema.newValidator().validate(new StreamSource(xml));
            LOGGER.info("XML відповідає схемі");
            return true;
        } catch (SAXParseException e) {
            LOGGER.error("XML не відповідає схемі (рядок {}, колонка {}): {}",
                    e.getLineNumber(), e.getColumnNumber(), e.getMessage());
            return false;
        } catch (SAXException | IOException e) {
            LOGGER.error("XML не вдалося перевірити: {}", e.getMessage());
            return false;
        }
    }
}
