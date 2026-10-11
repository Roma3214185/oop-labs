package ua.medicine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ua.medicine.TestData;

class ModelTest {

    /**
     * Checks equals and hashCode: equal copies are equal, and changing
     * any single property breaks equality.
     */
    @SafeVarargs
    private static <T> void assertEqualsContract(Supplier<T> factory,
            Consumer<T>... modifications) {
        T value = factory.get();
        assertEquals(value, value);
        assertEquals(value, factory.get());
        assertEquals(value.hashCode(), factory.get().hashCode());
        assertNotEquals(value, null);
        assertNotEquals(value, new Object());
        for (Consumer<T> modification : modifications) {
            T changed = factory.get();
            modification.accept(changed);
            assertNotEquals(value, changed);
        }
    }

    @Test
    void certificateEquality() {
        assertEqualsContract(() -> new Certificate("UA/0001/01/01",
                        LocalDate.of(2020, 1, 1), LocalDate.of(2025, 1, 1),
                        "MOH"),
                c -> c.setNumber("UA/0002/01/01"),
                c -> c.setIssueDate(LocalDate.of(2019, 1, 1)),
                c -> c.setExpiryDate(LocalDate.of(2026, 1, 1)),
                c -> c.setRegisteredBy("Other"));
    }

    @Test
    void certificateValidity() {
        Certificate certificate = new Certificate("UA/0001/01/01",
                LocalDate.of(2020, 1, 1), LocalDate.of(2025, 1, 1), "MOH");

        assertTrue(certificate.isValidOn(LocalDate.of(2020, 1, 1)));
        assertTrue(certificate.isValidOn(LocalDate.of(2025, 1, 1)));
        assertFalse(certificate.isValidOn(LocalDate.of(2019, 12, 31)));
        assertFalse(certificate.isValidOn(LocalDate.of(2025, 1, 2)));
        assertEquals("UA/0001/01/01", certificate.getNumber());
        assertEquals("MOH", certificate.getRegisteredBy());
        assertEquals("Certificate{UA/0001/01/01, 2020-01-01 - 2025-01-01,"
                + " MOH}", certificate.toString());
    }

    @Test
    void packageEquality() {
        assertEqualsContract(() -> new DrugPackage(PackageType.BOX, 10,
                        new BigDecimal("9.99"), Currency.UAH),
                p -> p.setType(PackageType.BOTTLE),
                p -> p.setQuantity(11),
                p -> p.setPrice(BigDecimal.ONE),
                p -> p.setCurrency(Currency.USD));
    }

    @Test
    void packageDefaultsToHryvnia() {
        DrugPackage drugPackage = new DrugPackage();

        assertEquals(Currency.UAH, drugPackage.getCurrency());
        assertEquals("Package{null x0, null UAH}", drugPackage.toString());
    }

    @Test
    void dosageEqualityAndDailyAmount() {
        assertEqualsContract(() -> new Dosage(new BigDecimal("250"),
                        DosageUnit.MG, 3),
                d -> d.setAmount(BigDecimal.TEN),
                d -> d.setUnit(DosageUnit.G),
                d -> d.setPerDay(2));
        Dosage dosage = new Dosage(new BigDecimal("250"), DosageUnit.MG, 3);

        assertEquals(new BigDecimal("750"), dosage.getDailyAmount());
        assertEquals("Dosage{250 MG x3/day}", dosage.toString());
    }

    @Test
    void producerEquality() {
        assertEqualsContract(() -> new Producer("Farmak", new Certificate(),
                        new DrugPackage(), new Dosage()),
                p -> p.setName("Other"),
                p -> p.setCertificate(null),
                p -> p.setDrugPackage(null),
                p -> p.setDosage(null));
        assertTrue(new Producer().toString().startsWith("Producer{null"));
    }

    @Test
    void versionEquality() {
        assertEqualsContract(() -> {
            Version version = new Version(DrugForm.POWDER);
            version.addProducer(new Producer());
            return version;
        },
                v -> v.setForm(DrugForm.DROPS),
                v -> v.addProducer(new Producer()));
        assertEquals("Version{null, []}", new Version().toString());
    }

    @Test
    void versionProducersAreReadOnly() {
        Version version = new Version();

        assertThrows(UnsupportedOperationException.class,
                () -> version.getProducers().add(new Producer()));
        assertThrows(NullPointerException.class,
                () -> version.addProducer(null));
    }

    @Test
    void drugEquality() {
        assertEqualsContract(TestData::aevit,
                d -> d.setId("drug-999"),
                d -> d.setName("Other"),
                d -> d.setPharm("Other"),
                d -> d.setGroup(DrugGroup.ANTIBIOTICS),
                d -> d.setPrescription(true),
                d -> d.addAnalog("Other"),
                d -> d.addVersion(new Version()));
    }

    @Test
    void drugAccessors() {
        Drug drug = new Drug();
        drug.addAnalog("A");

        assertEquals(List.of("A"), drug.getAnalogs());
        assertThrows(UnsupportedOperationException.class,
                () -> drug.getAnalogs().clear());
        assertThrows(UnsupportedOperationException.class,
                () -> drug.getVersions().clear());
        assertThrows(NullPointerException.class, () -> drug.addAnalog(null));
        assertThrows(NullPointerException.class,
                () -> drug.addVersion(null));
        assertTrue(TestData.aevit().toString()
                .startsWith("Drug{id=drug-005, name=Aevit"));
    }

    @Test
    void gettersReturnParsedValues() {
        Drug drug = TestData.aevit();
        Producer producer = drug.getVersions().get(0).getProducers().get(0);
        Certificate certificate = producer.getCertificate();
        DrugPackage drugPackage = producer.getDrugPackage();
        Dosage dosage = producer.getDosage();

        assertEquals("Kyiv Vitamin Plant", drug.getPharm());
        assertEquals(DrugForm.CAPSULES, drug.getVersions().get(0).getForm());
        assertEquals(LocalDate.of(2023, 2, 28), certificate.getIssueDate());
        assertEquals(LocalDate.of(2028, 2, 28), certificate.getExpiryDate());
        assertEquals(PackageType.BLISTER, drugPackage.getType());
        assertEquals(20, drugPackage.getQuantity());
        assertEquals(new BigDecimal("45.80"), drugPackage.getPrice());
        assertEquals(new BigDecimal("200"), dosage.getAmount());
        assertEquals(DosageUnit.MG, dosage.getUnit());
        assertEquals(1, dosage.getPerDay());
    }

    @Test
    void minPriceIsCheapestPackage() {
        Drug drug = TestData.drug("d", "D", DrugGroup.VITAMINS, "30.00");
        Version version = new Version(DrugForm.SYRUP);
        Producer cheap = new Producer();
        cheap.setDrugPackage(new DrugPackage(PackageType.BOTTLE, 1,
                new BigDecimal("12.50"), Currency.UAH));
        version.addProducer(cheap);
        Producer withoutPrice = new Producer();
        withoutPrice.setDrugPackage(new DrugPackage());
        version.addProducer(withoutPrice);
        drug.addVersion(version);

        assertEquals(Optional.of(new BigDecimal("12.50")),
                drug.getMinPrice());
    }

    @Test
    void minPriceIsEmptyWithoutPackages() {
        assertEquals(Optional.empty(),
                TestData.drug("d", "D", DrugGroup.VITAMINS, null)
                        .getMinPrice());
        assertEquals(Optional.empty(), new Drug().getMinPrice());
    }
}
