package ua.medicine;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;

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

/**
 * Shared test fixtures.
 */
public final class TestData {

    /** Main XML document of the application. */
    public static final Path XML = Path.of("data", "medicines.xml");
    /** Schema of the document. */
    public static final Path XSD = Path.of("data", "medicines.xsd");
    /** Grouping stylesheet. */
    public static final Path XSL = Path.of("data", "medicines-by-group.xsl");

    private static final String MOH = "Ministry of Health of Ukraine";

    private TestData() {
    }

    /**
     * Resolves a test resource to a file path.
     *
     * @param name resource name relative to the {@code xml} folder
     * @return path
     */
    public static Path resource(String name) {
        try {
            return Path.of(TestData.class.getResource("/xml/" + name)
                    .toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the drug {@code drug-005} exactly as written in the XML.
     *
     * @return expected drug
     */
    public static Drug aevit() {
        Drug drug = new Drug("drug-005", "Aevit", "Kyiv Vitamin Plant",
                DrugGroup.VITAMINS, false);
        Version capsules = new Version(DrugForm.CAPSULES);
        capsules.addProducer(new Producer("Kyiv Vitamin Plant",
                new Certificate("UA/5678/01/01", LocalDate.of(2023, 2, 28),
                        LocalDate.of(2028, 2, 28), MOH),
                new DrugPackage(PackageType.BLISTER, 20,
                        new BigDecimal("45.80"), Currency.UAH),
                new Dosage(new BigDecimal("200"), DosageUnit.MG, 1)));
        drug.addVersion(capsules);
        return drug;
    }

    /**
     * Returns the drug of {@code minimal.xml}.
     *
     * @return expected drug
     */
    public static Drug validol() {
        Drug drug = new Drug("drug-100", "Validol", "Darnytsia & Co",
                DrugGroup.ANALGESICS, false);
        Version drops = new Version(DrugForm.DROPS);
        drops.addProducer(new Producer("Darnytsia",
                new Certificate("UA/0001/01/01", LocalDate.of(2020, 1, 1),
                        LocalDate.of(2030, 1, 1), MOH),
                new DrugPackage(PackageType.BOTTLE, 1,
                        new BigDecimal("25.50"), Currency.UAH),
                new Dosage(new BigDecimal("5"), DosageUnit.DROPS, 3)));
        drug.addVersion(drops);
        return drug;
    }

    /**
     * Creates a drug with a single package price.
     *
     * @param id    identifier
     * @param name  name
     * @param group group
     * @param price price or null for a producer without a package
     * @return drug
     */
    public static Drug drug(String id, String name, DrugGroup group,
            String price) {
        Drug drug = new Drug(id, name, "Pharm", group, false);
        Version version = new Version(DrugForm.TABLETS);
        Producer producer = new Producer();
        if (price != null) {
            producer.setDrugPackage(new DrugPackage(PackageType.BOX, 1,
                    new BigDecimal(price), Currency.UAH));
        }
        version.addProducer(producer);
        drug.addVersion(version);
        return drug;
    }
}
