package ua.medicine.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Dosage of a drug and how many times a day it is taken.
 */
public class Dosage {

    private BigDecimal amount;
    private DosageUnit unit;
    private int perDay;

    public Dosage() {
    }

    public Dosage(BigDecimal amount, DosageUnit unit, int perDay) {
        this.amount = amount;
        this.unit = unit;
        this.perDay = perDay;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DosageUnit getUnit() {
        return unit;
    }

    public void setUnit(DosageUnit unit) {
        this.unit = unit;
    }

    public int getPerDay() {
        return perDay;
    }

    public void setPerDay(int perDay) {
        this.perDay = perDay;
    }

    /**
     * Calculates the amount of the drug taken during one day.
     *
     * @return daily amount in {@link #getUnit() units}
     */
    public BigDecimal getDailyAmount() {
        return amount.multiply(BigDecimal.valueOf(perDay));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dosage other)) {
            return false;
        }
        return perDay == other.perDay
                && unit == other.unit
                && Objects.equals(amount, other.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, unit, perDay);
    }

    @Override
    public String toString() {
        return "Dosage{" + amount + " " + unit + " x" + perDay
                + "/day}";
    }
}
