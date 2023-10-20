package hr.solujic.leapwise.expression;

import com.fasterxml.jackson.databind.JsonNode;
import hr.solujic.leapwise.enums.ExpressionOperator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ExpressionHandlerAbstract implements IExpressionHandler {

  final ExpressionOperator expressionOperator;
  protected String fieldName;
  protected String fieldValue;

  protected ExpressionHandlerAbstract(ExpressionOperator expressionOperator) {
    this.expressionOperator = expressionOperator;
  }

  public void parseFieldNameAndFieldValue(String expression) {
    this.fieldName = expression.split(this.getExpressionOperator().getValue())[0].trim();
    this.fieldValue =
        expression.split(this.getExpressionOperator().getValue())[1].replace("\"", "").trim();
  }

  @Override
  public abstract boolean evaluateExpression(String expression, JsonNode node);

  @Override
  public abstract boolean supportsExpression(String expression);
}
