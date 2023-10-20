package hr.solujic.leapwise.expression.handlers;

import static hr.solujic.leapwise.util.JsonUtil.getFieldValueForPath;

import com.fasterxml.jackson.databind.JsonNode;
import hr.solujic.leapwise.enums.ExpressionOperator;
import hr.solujic.leapwise.expression.ExpressionHandlerAbstract;
import org.springframework.stereotype.Component;

@Component
public class GreaterThanExpressionHandler extends ExpressionHandlerAbstract {

  public GreaterThanExpressionHandler() {
    super(ExpressionOperator.GREATER_THAN);
  }

  @Override
  public boolean evaluateExpression(String expression, JsonNode node) {
    parseFieldNameAndFieldValue(expression);

    return Integer.parseInt((String) getFieldValueForPath(node, fieldName))
        > Integer.parseInt(fieldValue);
  }

  @Override
  public boolean supportsExpression(String expression) {
    return expression.contains(ExpressionOperator.GREATER_THAN.getValue())
        && !expression.contains(ExpressionOperator.GREATER_THAN_OR_EQUAL.getValue());
  }
}
