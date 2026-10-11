package ua.medicine.model;

/**
 * Form (consistency) of a drug version.
 */
public enum DrugForm implements XmlValue {
    TABLETS("tablets"),
    CAPSULES("capsules"),
    POWDER("powder"),
    DROPS("drops"),
    SYRUP("syrup"),
    INJECTION("injection");

    private final String value;

    DrugForm(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
