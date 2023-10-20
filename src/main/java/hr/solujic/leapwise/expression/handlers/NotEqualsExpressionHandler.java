package hr.solujic.leapwise.expression.handlers;

import static hr.solujic.leapwise.constants.ExpressionConstant.NULL;
import static hr.solujic.leapwise.util.JsonUtil.checkIfJsonObjectExists;
import static hr.solujic.leapwise.util.JsonUtil.getFieldValueForPath;

import com.fasterxml.jackson.databind.JsonNode;
import hr.solujic.leapwise.constants.ExpressionConstant;
import hr.solujic.leapwise.enums.ExpressionOperator;
import hr.solujic.leapwise.expression.ExpressionHandlerAbstract;
import org.springframework.stereotype.Component;

@Component
public class NotEqualsExpressionHandler extends ExpressionHandlerAbstract {

  public NotEqualsExpressionHandler() {
    super(ExpressionOperator.NOT_EQUALS);
  }

  @Override
  public boolean evaluateExpression(String expression, JsonNode node) {
    boolean exists;
    boolean expressionResult;

    parseFieldNameAndFieldValue(expression);

    // case when null
    if (fieldValue.equals(NULL)) {
      exists = checkIfJsonObjectExists(node, fieldName);
      expressionResult = exists;
    } else expressionResult = !getFieldValueForPath(node, fieldName).equals(fieldValue);
    return expressionResult;
  }

  @Override
  public boolean supportsExpression(String expression) {
    return expression.contains(ExpressionOperator.NOT_EQUALS.getValue());
  }
}
