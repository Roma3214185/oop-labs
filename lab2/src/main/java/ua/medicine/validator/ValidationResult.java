package ua.medicine.validator;

import java.util.List;

/**
 * Result of validating an XML document against a schema.
 *
 * @param errors problems found in the document, empty if it is valid
 */
public record ValidationResult(List<String> errors) {

    /**
     * Creates a result with an immutable copy of the errors.
     */
    public ValidationResult {
        errors = List.copyOf(errors);
    }

    /**
     * Tells whether the document conforms to the schema.
     *
     * @return true if no errors were found
     */
    public boolean isValid() {
        return errors.isEmpty();
    }
}
