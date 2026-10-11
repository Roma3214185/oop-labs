package ua.medicine.transform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.medicine.exception.XmlProcessingException;

/**
 * Transforms XML documents with XSL stylesheets.
 */
public class XslTransformer {

    private static final Logger LOGGER =
            LogManager.getLogger(XslTransformer.class);

    private static final String INDENT_AMOUNT =
            "{http://xml.apache.org/xslt}indent-amount";
    /** Puts the root element on a new line after the XML declaration. */
    private static final String IS_STANDALONE =
            "http://www.oracle.com/xml/is-standalone";

    /**
     * Applies the stylesheet to the document and writes the result.
     *
     * @param xml    source XML document
     * @param xsl    XSL stylesheet
     * @param output destination file, parent folders are created
     * @throws XmlProcessingException if the transformation fails
     */
    public void transform(Path xml, Path xsl, Path output)
            throws XmlProcessingException {
        LOGGER.debug("Transforming {} with {}", xml, xsl);
        try {
            Path parent = output.toAbsolutePath().getParent();
            Files.createDirectories(parent);
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET,
                    "");
            Transformer transformer = factory.newTransformer(
                    new StreamSource(xsl.toFile()));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(INDENT_AMOUNT, "4");
            transformer.setOutputProperty(IS_STANDALONE, "yes");
            transformer.transform(new StreamSource(xml.toFile()),
                    new StreamResult(output.toFile()));
        } catch (TransformerException | IOException e) {
            LOGGER.error("Transformation of {} failed: {}", xml,
                    e.getMessage());
            throw new XmlProcessingException(
                    "Cannot transform " + xml + ": " + e.getMessage(), e);
        }
        LOGGER.info("{} transformed into {}", xml, output);
    }
}
