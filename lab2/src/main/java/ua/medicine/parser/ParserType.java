package ua.medicine.parser;

import java.util.function.Supplier;

/**
 * Available XML parser implementations.
 */
public enum ParserType {
    SAX(SaxDrugParser::new),
    STAX(StaxDrugParser::new),
    DOM(DomDrugParser::new);

    private final Supplier<DrugParser> constructor;

    ParserType(Supplier<DrugParser> constructor) {
        this.constructor = constructor;
    }

    /**
     * Creates a new parser of this type.
     *
     * @return parser
     */
    public DrugParser createParser() {
        return constructor.get();
    }
}
