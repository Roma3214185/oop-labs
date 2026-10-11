package ua.medicine.model;

/**
 * Unit of a single dose.
 */
public enum DosageUnit implements XmlValue {
    MG("mg"),
    MCG("mcg"),
    G("g"),
    ML("ml"),
    DROPS("drops");

    private final String value;

    DosageUnit(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
