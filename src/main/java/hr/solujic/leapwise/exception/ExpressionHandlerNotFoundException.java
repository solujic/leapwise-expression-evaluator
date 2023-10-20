package hr.solujic.leapwise.exception;

public class ExpressionHandlerNotFoundException extends RuntimeException {

  public ExpressionHandlerNotFoundException(String expression) {
    super(String.format("The expression handler for expression (%s) was not found!", expression));
  }
}
