package ua.medicine.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Version (form) of a drug made by one or several producers.
 */
public class Version {

    private DrugForm form;
    private final List<Producer> producers = new ArrayList<>();

    public Version() {
    }

    public Version(DrugForm form) {
        this.form = form;
    }

    public DrugForm getForm() {
        return form;
    }

    public void setForm(DrugForm form) {
        this.form = form;
    }

    public List<Producer> getProducers() {
        return Collections.unmodifiableList(producers);
    }

    public void addProducer(Producer producer) {
        producers.add(Objects.requireNonNull(producer, "producer"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Version other)) {
            return false;
        }
        return form == other.form && producers.equals(other.producers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(form, producers);
    }

    @Override
    public String toString() {
        return "Version{" + form + ", " + producers + "}";
    }
}
