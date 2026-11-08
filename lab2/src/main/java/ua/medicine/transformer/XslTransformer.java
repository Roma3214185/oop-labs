package ua.medicine.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.medicine.XmlException;

/**
 * Перетворює XML-документ за XSL-шаблоном і записує результат у файл.
 */
public class XslTransformer {

    private static final Logger LOGGER =
            LogManager.getLogger(XslTransformer.class);

    /**
     * Виконує перетворення.
     *
     * @param xml    вхідний XML-документ
     * @param xsl    XSL-шаблон
     * @param output файл для результату (папки створюються за потреби)
     * @throws XmlException якщо шаблон некоректний або файл не записати
     */
    public void transform(InputStream xml, InputStream xsl, Path output)
            throws XmlException {
        try {
            Files.createDirectories(output.toAbsolutePath().getParent());
            TransformerFactory.newInstance()
                    .newTransformer(new StreamSource(xsl))
                    .transform(new StreamSource(xml),
                            new StreamResult(output.toFile()));
        } catch (IOException | TransformerException e) {
            throw new XmlException(
                    "XSL-перетворення не вдалося: " + e.getMessage(), e);
        }
        LOGGER.info("Результат XSL-перетворення записано в {}", output);
    }
}
