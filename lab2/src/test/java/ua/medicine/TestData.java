package ua.medicine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

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
 * Невеликий валідний XML для тестів і препарати, які з нього мають вийти.
 */
public final class TestData {

    /** Перший препарат: два аналоги, два виробники в одному варіанті. */
    public static final String AMOXIL = """
            <drug id="d1">
                <name>Амоксил</name>
                <pharm>GlaxoSmithKline</pharm>
                <group>ANTIBIOTIC</group>
                <analogs>
                    <analog>Флемоксин</analog>
                    <analog>Оспамокс</analog>
                </analogs>
                <versions>
                    <version form="TABLETS">
                        <producer name="GSK UK">
                            <certificate number="UA/1001/01" issueDate="2021-03-15" expiryDate="2026-03-15" registeredBy="МОЗ України"/>
                            <package type="BLISTER" quantity="20" price="185.40"/>
                            <dosage amount="500" unit="MG" perDay="3"/>
                        </producer>
                        <producer name="Teva">
                            <certificate number="UA/1001/02" issueDate="2022-06-01" expiryDate="2027-06-01" registeredBy="МОЗ України"/>
                            <package type="BLISTER" quantity="16" price="142.10"/>
                            <dosage amount="500" unit="MG" perDay="3"/>
                        </producer>
                    </version>
                </versions>
            </drug>
            """;

    /** Другий препарат: без аналогів, елементи в іншому порядку. */
    public static final String ASCORBIC = """
            <drug id="d2">
                <group>VITAMIN</group>
                <name>Аскорбінова кислота</name>
                <pharm>Дарниця</pharm>
                <versions>
                    <version form="DROPS">
                        <producer name="Дарниця">
                            <dosage amount="2.5" unit="ML" perDay="2"/>
                            <package type="BOTTLE" quantity="1" price="25.00"/>
                            <certificate number="UA/2002/01" issueDate="2023-01-20" expiryDate="2028-01-20" registeredBy="МОЗ України"/>
                        </producer>
                    </version>
                </versions>
            </drug>
            """;

    /** Валідний документ з обома препаратами. */
    public static final String VALID_XML = medicine(AMOXIL, ASCORBIC);

    private TestData() {
    }

    /**
     * Складає документ із фрагментів препаратів.
     *
     * @param drugs XML препаратів
     * @return повний документ
     */
    public static String medicine(String... drugs) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Medicine>\n"
                + String.join("", drugs) + "</Medicine>\n";
    }

    /**
     * Перетворює рядок на потік UTF-8.
     *
     * @param text вміст
     * @return потік
     */
    public static InputStream stream(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Препарати, які мають вийти з {@link #VALID_XML}.
     *
     * @return список препаратів
     */
    public static List<Drug> expectedDrugs() {
        return List.of(amoxil(), ascorbic());
    }

    /**
     * Препарат з {@link #AMOXIL}.
     *
     * @return препарат
     */
    public static Drug amoxil() {
        Drug drug = drug("d1", "Амоксил", "GlaxoSmithKline",
                DrugGroup.ANTIBIOTIC);
        drug.addAnalog("Флемоксин");
        drug.addAnalog("Оспамокс");
        Version tablets = new Version(DrugForm.TABLETS);
        tablets.addProducer(producer("GSK UK",
                new Certificate("UA/1001/01", LocalDate.of(2021, 3, 15),
                        LocalDate.of(2026, 3, 15), "МОЗ України"),
                new DrugPackage(PackageType.BLISTER, 20, 185.40),
                new Dosage(500, DosageUnit.MG, 3)));
        tablets.addProducer(producer("Teva",
                new Certificate("UA/1001/02", LocalDate.of(2022, 6, 1),
                        LocalDate.of(2027, 6, 1), "МОЗ України"),
                new DrugPackage(PackageType.BLISTER, 16, 142.10),
                new Dosage(500, DosageUnit.MG, 3)));
        drug.addVersion(tablets);
        return drug;
    }

    /**
     * Препарат з {@link #ASCORBIC}.
     *
     * @return препарат
     */
    public static Drug ascorbic() {
        Drug drug = drug("d2", "Аскорбінова кислота", "Дарниця",
                DrugGroup.VITAMIN);
        Version drops = new Version(DrugForm.DROPS);
        drops.addProducer(producer("Дарниця",
                new Certificate("UA/2002/01", LocalDate.of(2023, 1, 20),
                        LocalDate.of(2028, 1, 20), "МОЗ України"),
                new DrugPackage(PackageType.BOTTLE, 1, 25.00),
                new Dosage(2.5, DosageUnit.ML, 2)));
        drug.addVersion(drops);
        return drug;
    }

    /**
     * Створює препарат без варіантів і аналогів.
     *
     * @param id    ідентифікатор
     * @param name  назва
     * @param pharm фірма
     * @param group група
     * @return препарат
     */
    public static Drug drug(String id, String name, String pharm,
            DrugGroup group) {
        Drug drug = new Drug();
        drug.setId(id);
        drug.setName(name);
        drug.setPharm(pharm);
        drug.setGroup(group);
        return drug;
    }

    /**
     * Створює препарат з одним виробником і заданою ціною.
     *
     * @param name  назва
     * @param group група
     * @param price ціна упаковки
     * @return препарат
     */
    public static Drug drugWithPrice(String name, DrugGroup group,
            double price) {
        Drug drug = drug("id", name, "Фірма", group);
        Version version = new Version(DrugForm.TABLETS);
        version.addProducer(producer("Виробник", null,
                new DrugPackage(PackageType.BOX, 10, price), null));
        drug.addVersion(version);
        return drug;
    }

    private static Producer producer(String name, Certificate certificate,
            DrugPackage drugPackage, Dosage dosage) {
        Producer producer = new Producer(name);
        producer.setCertificate(certificate);
        producer.setDrugPackage(drugPackage);
        producer.setDosage(dosage);
        return producer;
    }
}
