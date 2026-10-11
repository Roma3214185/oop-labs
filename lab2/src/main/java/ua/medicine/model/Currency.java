package ua.medicine.model;

/**
 * Currency of a package price.
 */
public enum Currency implements XmlValue {
    UAH("UAH"),
    USD("USD"),
    EUR("EUR");

    private final String value;

    Currency(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
