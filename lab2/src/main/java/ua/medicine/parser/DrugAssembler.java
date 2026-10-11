package ua.medicine.parser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ua.medicine.model.Certificate;
import ua.medicine.model.Currency;
import ua.medicine.model.Dosage;
import ua.medicine.model.DosageUnit;
import ua.medicine.model.Drug;
import ua.medicine.model.DrugForm;
import ua.medicine.model.DrugGroup;
import ua.medicine.model.DrugPackage;
import ua.medicine.model.PackageType;
import ua.medicine.model.Producer;
import ua.medicine.model.Version;
import ua.medicine.model.XmlValue;

/**
 * Builds {@link Drug} objects from a stream of element events.
 *
 * <p>The assembler is shared by the SAX, StAX and DOM parsers, so the
 * mapping between XML and the object model is written only once.
 */
public class DrugAssembler {

    private final List<Drug> drugs = new ArrayList<>();

    private Drug drug;
    private Version version;
    private Producer producer;
    private Certificate certificate;
    private DrugPackage drugPackage;
    private Dosage dosage;

    /**
     * Handles the opening tag of an element.
     *
     * @param tagName    local name of the element
     * @param attributes attributes of the element by local name
     */
    public void startElement(String tagName,
            Map<String, String> attributes) {
        switch (DrugXmlTag.of(tagName)) {
            case DRUG -> {
                drug = new Drug();
                drug.setId(attributes.get(DrugXmlTag.ATTR_ID));
                drug.setPrescription(Boolean.parseBoolean(
                        attributes.get(DrugXmlTag.ATTR_PRESCRIPTION)));
            }
            case VERSION -> version = new Version(XmlValue.fromValue(
                    DrugForm.class, attributes.get(DrugXmlTag.ATTR_FORM)));
            case PRODUCER -> {
                producer = new Producer();
                producer.setName(attributes.get(DrugXmlTag.ATTR_NAME));
            }
            case CERTIFICATE -> certificate = new Certificate();
            case PACKAGE -> {
                drugPackage = new DrugPackage();
                drugPackage.setType(XmlValue.fromValue(PackageType.class,
                        attributes.get(DrugXmlTag.ATTR_TYPE)));
            }
            case PRICE -> {
                String currency = attributes.get(DrugXmlTag.ATTR_CURRENCY);
                if (currency != null) {
                    drugPackage.setCurrency(
                            XmlValue.fromValue(Currency.class, currency));
                }
            }
            case DOSAGE -> dosage = new Dosage();
            case AMOUNT -> dosage.setUnit(XmlValue.fromValue(
                    DosageUnit.class, attributes.get(DrugXmlTag.ATTR_UNIT)));
            default -> {
                // container elements carry no data of their own
            }
        }
    }

    /**
     * Handles the closing tag of an element.
     *
     * @param tagName local name of the element
     * @param text    text content of the element
     */
    public void endElement(String tagName, String text) {
        String value = text == null ? "" : text.trim();
        switch (DrugXmlTag.of(tagName)) {
            case DRUG -> drugs.add(drug);
            case NAME -> drug.setName(value);
            case PHARM -> drug.setPharm(value);
            case GROUP -> drug.setGroup(
                    XmlValue.fromValue(DrugGroup.class, value));
            case ANALOG -> drug.addAnalog(value);
            case VERSION -> drug.addVersion(version);
            case PRODUCER -> version.addProducer(producer);
            case CERTIFICATE -> producer.setCertificate(certificate);
            case NUMBER -> certificate.setNumber(value);
            case ISSUE_DATE -> certificate.setIssueDate(LocalDate.parse(value));
            case EXPIRY_DATE ->
                    certificate.setExpiryDate(LocalDate.parse(value));
            case REGISTERED_BY -> certificate.setRegisteredBy(value);
            case PACKAGE -> producer.setDrugPackage(drugPackage);
            case QUANTITY -> drugPackage.setQuantity(Integer.parseInt(value));
            case PRICE -> drugPackage.setPrice(new BigDecimal(value));
            case DOSAGE -> producer.setDosage(dosage);
            case AMOUNT -> dosage.setAmount(new BigDecimal(value));
            case PER_DAY -> dosage.setPerDay(Integer.parseInt(value));
            default -> {
                // container elements carry no data of their own
            }
        }
    }

    /**
     * Returns the drugs assembled so far.
     *
     * @return list of drugs
     */
    public List<Drug> getDrugs() {
        return new ArrayList<>(drugs);
    }
}
