package ua.medicine.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Drug described by the {@code drug} element of the XML document.
 */
public class Drug {

    private String id;
    private String name;
    private String pharm;
    private DrugGroup group;
    private boolean prescription;
    private final List<String> analogs = new ArrayList<>();
    private final List<Version> versions = new ArrayList<>();

    public Drug() {
    }

    public Drug(String id, String name, String pharm, DrugGroup group,
            boolean prescription) {
        this.id = id;
        this.name = name;
        this.pharm = pharm;
        this.group = group;
        this.prescription = prescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPharm() {
        return pharm;
    }

    public void setPharm(String pharm) {
        this.pharm = pharm;
    }

    public DrugGroup getGroup() {
        return group;
    }

    public void setGroup(DrugGroup group) {
        this.group = group;
    }

    public boolean isPrescription() {
        return prescription;
    }

    public void setPrescription(boolean prescription) {
        this.prescription = prescription;
    }

    public List<String> getAnalogs() {
        return Collections.unmodifiableList(analogs);
    }

    public void addAnalog(String analog) {
        analogs.add(Objects.requireNonNull(analog, "analog"));
    }

    public List<Version> getVersions() {
        return Collections.unmodifiableList(versions);
    }

    public void addVersion(Version version) {
        versions.add(Objects.requireNonNull(version, "version"));
    }

    /**
     * Finds the cheapest package price among all versions and producers.
     *
     * @return minimal price or empty if no prices are known
     */
    public Optional<BigDecimal> getMinPrice() {
        return versions.stream()
                .flatMap(v -> v.getProducers().stream())
                .map(Producer::getDrugPackage)
                .filter(Objects::nonNull)
                .map(DrugPackage::getPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Drug other)) {
            return false;
        }
        return prescription == other.prescription
                && Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(pharm, other.pharm)
                && group == other.group
                && analogs.equals(other.analogs)
                && versions.equals(other.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pharm, group, prescription, analogs,
                versions);
    }

    @Override
    public String toString() {
        return "Drug{id=" + id + ", name=" + name + ", pharm=" + pharm
                + ", group=" + group + ", prescription=" + prescription
                + ", analogs=" + analogs + ", versions=" + versions + "}";
    }
}
