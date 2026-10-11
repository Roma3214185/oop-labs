package ua.medicine.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Registration certificate of a drug.
 */
public class Certificate {

    private String number;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String registeredBy;

    public Certificate() {
    }

    public Certificate(String number, LocalDate issueDate,
            LocalDate expiryDate, String registeredBy) {
        this.number = number;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.registeredBy = registeredBy;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    /**
     * Checks whether the certificate is in force on the given date.
     *
     * @param date date to check
     * @return true if issued not later and expires not earlier than date
     */
    public boolean isValidOn(LocalDate date) {
        return !date.isBefore(issueDate) && !date.isAfter(expiryDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Certificate other)) {
            return false;
        }
        return Objects.equals(number, other.number)
                && Objects.equals(issueDate, other.issueDate)
                && Objects.equals(expiryDate, other.expiryDate)
                && Objects.equals(registeredBy, other.registeredBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, issueDate, expiryDate, registeredBy);
    }

    @Override
    public String toString() {
        return "Certificate{" + number + ", " + issueDate + " - "
                + expiryDate + ", " + registeredBy + "}";
    }
}
