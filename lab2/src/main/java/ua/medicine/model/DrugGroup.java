package ua.medicine.model;

/**
 * Pharmacological group of a drug.
 */
public enum DrugGroup implements XmlValue {
    ANTIBIOTICS("antibiotics"),
    ANALGESICS("analgesics"),
    ANTIPYRETICS("antipyretics"),
    VITAMINS("vitamins"),
    ANTIVIRALS("antivirals"),
    ANTIHISTAMINES("antihistamines");

    private final String value;

    DrugGroup(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
