package ua.medicine.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Package of a drug: kind, number of items and price.
 */
public class DrugPackage {

    private PackageType type;
    private int quantity;
    private BigDecimal price;
    private Currency currency = Currency.UAH;

    public DrugPackage() {
    }

    public DrugPackage(PackageType type, int quantity, BigDecimal price,
            Currency currency) {
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.currency = currency;
    }

    public PackageType getType() {
        return type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DrugPackage other)) {
            return false;
        }
        return quantity == other.quantity
                && type == other.type
                && currency == other.currency
                && Objects.equals(price, other.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, quantity, price, currency);
    }

    @Override
    public String toString() {
        return "Package{" + type + " x" + quantity + ", " + price + " "
                + currency + "}";
    }
}
