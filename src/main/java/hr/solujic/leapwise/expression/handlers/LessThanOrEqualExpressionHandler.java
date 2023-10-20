package hr.solujic.leapwise.expression.handlers;

import static hr.solujic.leapwise.util.JsonUtil.getFieldValueForPath;

import com.fasterxml.jackson.databind.JsonNode;
import hr.solujic.leapwise.enums.ExpressionOperator;
import hr.solujic.leapwise.expression.ExpressionHandlerAbstract;
import org.springframework.stereotype.Component;

@Component
public class LessThanOrEqualExpressionHandler extends ExpressionHandlerAbstract {

  public LessThanOrEqualExpressionHandler() {
    super(ExpressionOperator.LESS_THAN_OR_EQUAL);
  }

  @Override
  public boolean evaluateExpression(String expression, JsonNode node) {
    parseFieldNameAndFieldValue(expression);

    return Integer.parseInt((String) getFieldValueForPath(node, fieldName))
        <= Integer.parseInt(fieldValue);
  }

  @Override
  public boolean supportsExpression(String expression) {
    return expression.contains(ExpressionOperator.LESS_THAN_OR_EQUAL.getValue());
  }
}
