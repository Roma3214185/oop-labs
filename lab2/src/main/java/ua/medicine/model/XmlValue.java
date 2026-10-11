package ua.medicine.model;

/**
 * Enumeration constant that has a textual representation in XML.
 */
public interface XmlValue {

    /**
     * Returns the value used in the XML document.
     *
     * @return XML value
     */
    String value();

    /**
     * Finds the constant of the enumeration by its XML value.
     *
     * @param type  enumeration class
     * @param value XML value
     * @param <E>   enumeration type
     * @return matching constant
     * @throws IllegalArgumentException if no constant matches
     */
    static <E extends Enum<E> & XmlValue> E fromValue(Class<E> type,
            String value) {
        for (E constant : type.getEnumConstants()) {
            if (constant.value().equals(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Unknown "
                + type.getSimpleName() + " value: " + value);
    }
}
