package ua.medicine.model;

/**
 * Kind of a drug package.
 */
public enum PackageType implements XmlValue {
    BLISTER("blister"),
    BOTTLE("bottle"),
    BOX("box"),
    SACHET("sachet"),
    AMPOULE("ampoule");

    private final String value;

    PackageType(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
