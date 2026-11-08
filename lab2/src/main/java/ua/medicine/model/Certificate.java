package ua.medicine.model;

import java.time.LocalDate;

/**
 * Свідоцтво про реєстрацію препарату.
 *
 * @param number       номер свідоцтва, напр. UA/1001/01
 * @param issueDate    дата видачі
 * @param expiryDate   дата закінчення дії
 * @param registeredBy реєструюча організація
 */
public record Certificate(String number, LocalDate issueDate,
        LocalDate expiryDate, String registeredBy) {
}
