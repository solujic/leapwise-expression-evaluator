package hr.solujic.leapwise.exception;

public class ExpressionNotFoundException extends RuntimeException {

  public ExpressionNotFoundException(int expressionId) {
    super(String.format("The expression (id: %s) was not found in the database!", expressionId));
  }
}
