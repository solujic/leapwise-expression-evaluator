package hr.solujic.leapwise.exception;

public class WrongOperatorUsedInsideExpressionException extends RuntimeException {

  public WrongOperatorUsedInsideExpressionException(int expressionId) {
    super(
        String.format(
            "The expression (id: %s) that was sent for evaluation is faulty! "
                + "Can not use comparison operators for field of type string!",
            expressionId));
  }
}
