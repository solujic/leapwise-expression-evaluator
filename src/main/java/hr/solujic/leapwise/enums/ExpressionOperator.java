package hr.solujic.leapwise.enums;

public enum ExpressionOperator {
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    private final String value;

    ExpressionOperator(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
