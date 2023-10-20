package hr.solujic.leapwise.services;

import static hr.solujic.leapwise.constants.ExpressionConstant.*;

import com.fasterxml.jackson.databind.JsonNode;
import hr.solujic.leapwise.entity.Expression;
import hr.solujic.leapwise.exception.ExpressionHandlerNotFoundException;
import hr.solujic.leapwise.exception.ExpressionNotFoundException;
import hr.solujic.leapwise.exception.WrongOperatorUsedInsideExpressionException;
import hr.solujic.leapwise.expression.IExpressionHandler;
import hr.solujic.leapwise.repository.ExpressionRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExpressionEvaluationService {

  @Autowired private ExpressionRepository expressionRepository;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  @Autowired
  private List<IExpressionHandler> expressionHandlers;

  /***
   * Evaluates the logical expression based on the given expression ID and given JSON object.
   * @param expressionId - expression ID.
   * @param jsonObject - JSON Object.
   * @return - returns true if the expression is true and false if false.
   */
  public Boolean evaluateExpression(Integer expressionId, JsonNode jsonObject) {
    var expressionOptional = expressionRepository.findById(expressionId);
    if (expressionOptional.isEmpty()) {
      log.error(String.format("Expression with id was not found %s!!", expressionId));
      throw new ExpressionNotFoundException(expressionId);
    }
    var expression = expressionOptional.get();

    Boolean result = null;
    try {
      log.debug("Starting to evaluate expression...");
      result = executeExpressionLogic(expression, jsonObject);
    } catch (NumberFormatException e) {
      log.error(
          String.format(
              "Something went wrong when evaluating expression with id (%s)", expressionId));
      // case when somebody tries to use comparison operators (>, <, >=, <=) with string fields.
      throw new WrongOperatorUsedInsideExpressionException(expressionId);
    }

    log.debug(
        String.format("Expression (%s) was successfully evaluated: %s", expressionId, result));
    return result;
  }

  private Boolean executeExpressionLogic(Expression expression, JsonNode node) {
    // split expression into array by OR operator
    var expressionArray = splitExpressionByOrOperator(expression);

    Boolean expressionResult = null;
    Boolean expressionResultPrevious = null;
    for (var e : expressionArray) {
      // case for nested expressions with && operators
      if (e.contains(AND_OPERATOR)) {
        var andOperatorExpressions = e.split(AND_OPERATOR);
        for (var s : andOperatorExpressions) {
          expressionResult = evaluateSingleExpressionByOperators(node, s);
          // case when false, skip rest of the processing (since we're using and operation here)
          if (!expressionResult) {
            break;
          }
        }
      } else {
        // case when no && operators are used within whole expression, call single expression
        // evaluation.
        expressionResult = evaluateSingleExpressionByOperators(node, e);
      }

      // CASE FOR OR operation
      if (expressionResultPrevious != null && expressionResultPrevious && !expressionResult) {
        expressionResult = true;
      }

      expressionResultPrevious = expressionResult;
    }
    return expressionResult;
  }

  private static String[] splitExpressionByOrOperator(Expression expression) {
    return expression.getExpression().replace("(", "").replace(")", "").split(OR_OPERATOR);
  }

  private Boolean evaluateSingleExpressionByOperators(JsonNode node, String expression) {
    return expressionHandlers.stream()
        .filter(iExpressionHandler -> iExpressionHandler.supportsExpression(expression))
        .findFirst()
        .orElseThrow(() -> new ExpressionHandlerNotFoundException(expression))
        .evaluateExpression(expression, node);
  }
}
