package ua.medicine.parser;

/**
 * Element and attribute names of the medicines XML document.
 */
public enum DrugXmlTag {
    MEDICINE("Medicine"),
    DRUG("drug"),
    NAME("name"),
    PHARM("pharm"),
    GROUP("group"),
    ANALOGS("analogs"),
    ANALOG("analog"),
    VERSIONS("versions"),
    VERSION("version"),
    PRODUCER("producer"),
    CERTIFICATE("certificate"),
    NUMBER("number"),
    ISSUE_DATE("issueDate"),
    EXPIRY_DATE("expiryDate"),
    REGISTERED_BY("registeredBy"),
    PACKAGE("package"),
    QUANTITY("quantity"),
    PRICE("price"),
    DOSAGE("dosage"),
    AMOUNT("amount"),
    PER_DAY("perDay");

    /** Attribute {@code id} of a drug. */
    public static final String ATTR_ID = "id";
    /** Attribute {@code prescription} of a drug. */
    public static final String ATTR_PRESCRIPTION = "prescription";
    /** Attribute {@code form} of a version. */
    public static final String ATTR_FORM = "form";
    /** Attribute {@code name} of a producer. */
    public static final String ATTR_NAME = "name";
    /** Attribute {@code type} of a package. */
    public static final String ATTR_TYPE = "type";
    /** Attribute {@code currency} of a price. */
    public static final String ATTR_CURRENCY = "currency";
    /** Attribute {@code unit} of an amount. */
    public static final String ATTR_UNIT = "unit";

    private final String tagName;

    DrugXmlTag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    /**
     * Finds the tag by its element name.
     *
     * @param tagName element name
     * @return tag
     * @throws IllegalArgumentException if the element is unknown
     */
    public static DrugXmlTag of(String tagName) {
        for (DrugXmlTag tag : values()) {
            if (tag.tagName.equals(tagName)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("Unknown element: " + tagName);
    }
}
