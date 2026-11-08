package ua.medicine.model;

/**
 * Упаковка препарату.
 *
 * @param type     тип упаковки
 * @param quantity кількість в упаковці
 * @param price    ціна за упаковку, грн
 */
public record DrugPackage(PackageType type, int quantity, double price) {
}
