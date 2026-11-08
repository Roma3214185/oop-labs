package ua.medicine.model;

/**
 * Дозування препарату.
 *
 * @param amount доза за один прийом
 * @param unit   одиниця вимірювання дози
 * @param perDay скільки разів на день приймати
 */
public record Dosage(double amount, DosageUnit unit, int perDay) {
}
