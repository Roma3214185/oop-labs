package ua.medicine;

/**
 * Помилка роботи з XML: не вдалося прочитати, завантажити схему чи
 * виконати XSL-перетворення.
 */
public class XmlException extends Exception {

    /**
     * Створює виключення з повідомленням.
     *
     * @param message опис помилки
     */
    public XmlException(String message) {
        super(message);
    }

    /**
     * Створює виключення з повідомленням і причиною.
     *
     * @param message опис помилки
     * @param cause   початкова помилка
     */
    public XmlException(String message, Throwable cause) {
        super(message, cause);
    }
}
