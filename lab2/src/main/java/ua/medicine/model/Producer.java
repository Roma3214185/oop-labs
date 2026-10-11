package ua.medicine.model;

import java.util.Objects;

/**
 * Producer of a drug version with its certificate, package and dosage.
 */
public class Producer {

    private String name;
    private Certificate certificate;
    private DrugPackage drugPackage;
    private Dosage dosage;

    public Producer() {
    }

    public Producer(String name, Certificate certificate,
            DrugPackage drugPackage, Dosage dosage) {
        this.name = name;
        this.certificate = certificate;
        this.drugPackage = drugPackage;
        this.dosage = dosage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public DrugPackage getDrugPackage() {
        return drugPackage;
    }

    public void setDrugPackage(DrugPackage drugPackage) {
        this.drugPackage = drugPackage;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public void setDosage(Dosage dosage) {
        this.dosage = dosage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producer other)) {
            return false;
        }
        return Objects.equals(name, other.name)
                && Objects.equals(certificate, other.certificate)
                && Objects.equals(drugPackage, other.drugPackage)
                && Objects.equals(dosage, other.dosage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, certificate, drugPackage, dosage);
    }

    @Override
    public String toString() {
        return "Producer{" + name + ", " + certificate + ", "
                + drugPackage + ", " + dosage + "}";
    }
}
