package ua.medicine.parser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ua.medicine.model.Certificate;
import ua.medicine.model.Dosage;
import ua.medicine.model.DosageUnit;
import ua.medicine.model.Drug;
import ua.medicine.model.DrugForm;
import ua.medicine.model.DrugGroup;
import ua.medicine.model.DrugPackage;
import ua.medicine.model.PackageType;
import ua.medicine.model.Producer;
import ua.medicine.model.Version;

/**
 * Збирає препарати з подій «тег відкрився» / «тег закрився».
 *
 * <p>Це єдине місце, де XML перетворюється на об'єкти. Парсери DOM, SAX
 * і StAX лише по-різному знаходять теги й передають їх сюди, тому код
 * розбору не дублюється.
 */
public class DrugBuilder {

    private final List<Drug> drugs = new ArrayList<>();
    private Drug drug;
    private Version version;
    private Producer producer;

    /**
     * Обробляє відкриття тегу: тут створюються об'єкти й читаються
     * атрибути.
     *
     * @param tag        назва тегу
     * @param attributes атрибути тегу
     */
    public void startElement(String tag, Map<String, String> attributes) {
        switch (tag) {
            case "drug" -> {
                drug = new Drug();
                drug.setId(attributes.get("id"));
                drugs.add(drug);
            }
            case "version" -> {
                version = new Version(
                        DrugForm.valueOf(attributes.get("form")));
                drug.addVersion(version);
            }
            case "producer" -> {
                producer = new Producer(attributes.get("name"));
                version.addProducer(producer);
            }
            case "certificate" -> producer.setCertificate(new Certificate(
                    attributes.get("number"),
                    LocalDate.parse(attributes.get("issueDate")),
                    LocalDate.parse(attributes.get("expiryDate")),
                    attributes.get("registeredBy")));
            case "package" -> producer.setDrugPackage(new DrugPackage(
                    PackageType.valueOf(attributes.get("type")),
                    Integer.parseInt(attributes.get("quantity")),
                    Double.parseDouble(attributes.get("price"))));
            case "dosage" -> producer.setDosage(new Dosage(
                    Double.parseDouble(attributes.get("amount")),
                    DosageUnit.valueOf(attributes.get("unit")),
                    Integer.parseInt(attributes.get("perDay"))));
            default -> {
                // Medicine, analogs, versions: даних не мають
            }
        }
    }

    /**
     * Обробляє закриття тегу: тут читається текст між тегами.
     *
     * @param tag  назва тегу
     * @param text текст усередині тегу
     */
    public void endElement(String tag, String text) {
        String value = text.trim();
        switch (tag) {
            case "name" -> drug.setName(value);
            case "pharm" -> drug.setPharm(value);
            case "group" -> drug.setGroup(DrugGroup.valueOf(value));
            case "analog" -> drug.addAnalog(value);
            default -> {
                // решта тегів тексту не мають
            }
        }
    }

    /**
     * Повертає зібрані препарати.
     *
     * @return список препаратів
     */
    public List<Drug> getDrugs() {
        return List.copyOf(drugs);
    }
}
