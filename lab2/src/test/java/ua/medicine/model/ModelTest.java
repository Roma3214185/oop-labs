package ua.medicine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ua.medicine.TestData;

class ModelTest {

    @Test
    void minPriceIsCheapestPackageOfAllProducers() {
        assertEquals(142.10, TestData.amoxil().getMinPrice());
        assertEquals(25.00, TestData.ascorbic().getMinPrice());
    }

    @Test
    void minPriceIsZeroWithoutProducers() {
        Drug drug = TestData.drug("d1", "Назва", "Фірма", DrugGroup.VITAMIN);
        drug.addVersion(new Version(DrugForm.SYRUP));

        assertEquals(0, drug.getMinPrice());
    }

    @Test
    void storesAllFields() {
        Drug drug = TestData.amoxil();
        Producer producer = drug.getVersions().get(0).getProducers().get(0);

        assertEquals("d1", drug.getId());
        assertEquals("Амоксил", drug.getName());
        assertEquals("GlaxoSmithKline", drug.getPharm());
        assertEquals(DrugGroup.ANTIBIOTIC, drug.getGroup());
        assertEquals(DrugForm.TABLETS, drug.getVersions().get(0).getForm());
        assertEquals("GSK UK", producer.getName());
        assertEquals("UA/1001/01", producer.getCertificate().number());
        assertEquals(20, producer.getDrugPackage().quantity());
        assertEquals(DosageUnit.MG, producer.getDosage().unit());
    }

    @Test
    void listsAreReadOnly() {
        Drug drug = TestData.amoxil();

        assertThrows(UnsupportedOperationException.class,
                () -> drug.getAnalogs().add("x"));
        assertThrows(UnsupportedOperationException.class,
                () -> drug.getVersions().clear());
        assertThrows(UnsupportedOperationException.class,
                () -> drug.getVersions().get(0).getProducers().clear());
    }

    @Test
    void equalDrugsHaveSameHashCode() {
        Drug drug = TestData.amoxil();

        assertEquals(drug, drug);
        assertEquals(TestData.amoxil(), drug);
        assertEquals(TestData.amoxil().hashCode(), drug.hashCode());
        assertNotEquals(TestData.ascorbic(), drug);
        assertFalse(drug.equals("Амоксил"));
    }

    /** Кожна зміна одного поля робить препарат нерівним початковому. */
    static Stream<Consumer<Drug>> changes() {
        return Stream.of(
                drug -> drug.setId("x"),
                drug -> drug.setName("x"),
                drug -> drug.setPharm("x"),
                drug -> drug.setGroup(DrugGroup.VITAMIN),
                drug -> drug.addAnalog("x"),
                drug -> drug.addVersion(new Version(DrugForm.DROPS)));
    }

    @ParameterizedTest
    @MethodSource("changes")
    void differentFieldMakesDrugsDifferent(Consumer<Drug> change) {
        Drug changed = TestData.amoxil();
        change.accept(changed);

        assertNotEquals(TestData.amoxil(), changed);
    }

    @Test
    void versionEquality() {
        Version version = TestData.amoxil().getVersions().get(0);
        Version other = new Version(DrugForm.TABLETS);

        assertEquals(version, version);
        assertEquals(TestData.amoxil().getVersions().get(0), version);
        assertEquals(TestData.amoxil().getVersions().get(0).hashCode(),
                version.hashCode());
        assertNotEquals(other, version);
        assertNotEquals(new Version(DrugForm.DROPS), other);
        assertFalse(version.equals("TABLETS"));
    }

    @Test
    void producerEquality() {
        Producer producer = new Producer("П");
        producer.setCertificate(TestData.amoxil().getVersions().get(0)
                .getProducers().get(0).getCertificate());

        assertEquals(producer, producer);
        assertFalse(producer.equals("П"));
        assertNotEquals(new Producer("Інший"), new Producer("П"));
        assertNotEquals(new Producer("П"), producer);

        Producer withPackage = new Producer("П");
        withPackage.setDrugPackage(new DrugPackage(PackageType.BOX, 1, 1));
        assertNotEquals(new Producer("П"), withPackage);

        Producer withDosage = new Producer("П");
        withDosage.setDosage(new Dosage(1, DosageUnit.G, 1));
        assertNotEquals(new Producer("П"), withDosage);

        assertEquals(new Producer("П"), new Producer("П"));
        assertEquals(new Producer("П").hashCode(),
                new Producer("П").hashCode());
    }

    @Test
    void toStringShowsContent() {
        String text = TestData.amoxil().toString();

        assertTrue(text.contains("name=Амоксил"));
        assertTrue(text.contains("form=TABLETS"));
        assertTrue(text.contains("name=GSK UK"));
        assertTrue(text.contains("price=185.4"));
    }
}
