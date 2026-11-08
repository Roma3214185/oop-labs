package ua.medicine.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Лікарський препарат — відповідає елементу {@code drug} зі схеми.
 */
public class Drug {

    private String id;
    private String name;
    private String pharm;
    private DrugGroup group;
    private final List<String> analogs = new ArrayList<>();
    private final List<Version> versions = new ArrayList<>();

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

    public List<String> getAnalogs() {
        return Collections.unmodifiableList(analogs);
    }

    public void addAnalog(String analog) {
        analogs.add(analog);
    }

    public List<Version> getVersions() {
        return Collections.unmodifiableList(versions);
    }

    public void addVersion(Version version) {
        versions.add(version);
    }

    /**
     * Повертає ціну найдешевшої упаковки серед усіх варіантів і виробників.
     *
     * @return мінімальна ціна, або 0, якщо виробників немає
     */
    public double getMinPrice() {
        return versions.stream()
                .flatMap(version -> version.getProducers().stream())
                .mapToDouble(producer -> producer.getDrugPackage().price())
                .min()
                .orElse(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Drug other)) {
            return false;
        }
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(pharm, other.pharm)
                && group == other.group
                && analogs.equals(other.analogs)
                && versions.equals(other.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pharm, group, analogs, versions);
    }

    @Override
    public String toString() {
        return "Drug{id=" + id + ", name=" + name + ", pharm=" + pharm
                + ", group=" + group + ", analogs=" + analogs
                + ", versions=" + versions + "}";
    }
}
