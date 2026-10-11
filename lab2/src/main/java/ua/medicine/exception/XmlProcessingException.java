package ua.medicine.exception;

/**
 * Signals that an XML document, schema or stylesheet cannot be
 * processed.
 */
public class XmlProcessingException extends Exception {

    private static final long serialVersionUID = 1L;

    public XmlProcessingException(String message) {
        super(message);
    }

    public XmlProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
