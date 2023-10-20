package hr.solujic.leapwise.expression.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import hr.solujic.leapwise.constants.ExpressionConstant;
import hr.solujic.leapwise.enums.ExpressionOperator;
import hr.solujic.leapwise.expression.ExpressionHandlerAbstract;
import org.springframework.stereotype.Component;

import static hr.solujic.leapwise.util.JsonUtil.checkIfJsonObjectExists;
import static hr.solujic.leapwise.util.JsonUtil.getFieldValueForPath;

@Component
public class EqualsExpressionHandler extends ExpressionHandlerAbstract {

  public EqualsExpressionHandler() {
    super(ExpressionOperator.EQUALS);
  }

  @Override
  public boolean evaluateExpression(String expression, JsonNode node) {
    boolean expressionResult;
    boolean exists;

    parseFieldNameAndFieldValue(expression);

    // case when null
    if (fieldValue.equals(ExpressionConstant.NULL)) {
      exists = checkIfJsonObjectExists(node, fieldName);
      expressionResult = !exists;
    } else expressionResult = getFieldValueForPath(node, fieldName).equals(fieldValue);
    return expressionResult;
  }

  @Override
  public boolean supportsExpression(String expression) {
    return expression.contains(ExpressionOperator.EQUALS.getValue());
  }
}
