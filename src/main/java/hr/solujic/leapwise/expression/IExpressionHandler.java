package hr.solujic.leapwise.expression;

import com.fasterxml.jackson.databind.JsonNode;
import hr.solujic.leapwise.enums.ExpressionOperator;

public interface IExpressionHandler {

  boolean evaluateExpression(String expression, JsonNode node);

  boolean supportsExpression(String expression);
}
