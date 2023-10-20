package hr.solujic.leapwise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.solujic.leapwise.entity.Expression;
import hr.solujic.leapwise.expression.IExpressionHandler;
import hr.solujic.leapwise.expression.handlers.*;
import hr.solujic.leapwise.repository.ExpressionRepository;
import hr.solujic.leapwise.services.ExpressionEvaluationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ExpressionEvaluationServiceTest {

  @Spy private List<IExpressionHandler> expressionHandlers;
  @InjectMocks private ExpressionEvaluationService expressionEvaluationService;
  @Mock private ExpressionRepository expressionRepositoryMock;

  @BeforeEach
  public void setUp() {
    expressionHandlers = new ArrayList<>();
    expressionHandlers.add(new EqualsExpressionHandler());
    expressionHandlers.add(new GreaterThanExpressionHandler());
    expressionHandlers.add(new GreaterThanOrEqualExpressionHandler());
    expressionHandlers.add(new LessThanExpressionHandler());
    expressionHandlers.add(new LessThanOrEqualExpressionHandler());
    expressionHandlers.add(new NotEqualsExpressionHandler());

    ReflectionTestUtils.setField(expressionEvaluationService,"expressionHandlers", expressionHandlers);
  }

  @Test
  public void evaluateSingleExpressionWithMultipleAndOperatorsTrueTest() throws IOException {
    var expression =
        "customer != null && customer.firstName == \"JOHN\" && customer.lastName == \"DOE\"";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionWithAndOperatorTrueTest() throws IOException {
    var expression = "customer != null && customer.firstName == \"JOHN\"";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionWithAndOperatorFalseTest() throws IOException {
    var expression = "customer != null && customer.firstName == \"JOHNNY\"";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionObjectNotNullTrueTest() throws IOException {
    var expression = "customer != null";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionObjectNotNullFalseTest() throws IOException {
    var expression = "random != null";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionObjectEqualsNullTrueTest() throws IOException {
    var expression = "random == null";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionObjectEqualsNullFalseTest() throws IOException {
    var expression = "customer == null";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionEqualsTrueTest() throws IOException {
    var expression = "customer.firstName == \"JOHN\"";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionEqualsFalseTest() throws IOException {
    var expression = "customer.firstName == \"JOHNNY\"";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionNotEqualsTrueTest() throws IOException {
    var expression = "customer.firstName != \"JOHNNY\"";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionNotEqualsFalseTest() throws IOException {
    var expression = "customer.firstName != \"JOHN\"";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionGreaterThanTrueTest() throws IOException {
    var expression = "customer.salary > 98";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionGreaterThanFalseTest() throws IOException {
    var expression = "customer.salary > 100";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionGreaterThanOrEqualTrueTest() throws IOException {
    var expression = "customer.salary >= 99";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionGreaterThanOrEqualFalseTest() throws IOException {
    var expression = "customer.salary >= 100";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionLessThanTrueTest() throws IOException {
    var expression = "customer.salary < 1000";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionLessThanFalseTest() throws IOException {
    var expression = "customer.salary < 50";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateSingleExpressionLessThanOrEqualTrueTest() throws IOException {
    var expression = "customer.salary <= 1000";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateSingleExpressionLessThanOrEqualFalseTest() throws IOException {
    var expression = "customer.salary <= 50";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  @Test
  public void evaluateExpressionOneOrStatementTest() throws IOException {
    var expression =
        "(customer.firstName == \"JOHN\" && customer.salary < 100)"
            + " OR (customer.address != null && customer.address.city == \"Washington\")";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateExpressionMultipleOrStatementsTest() throws IOException {
    var expression =
        "(customer.firstName == \"JOHN\" && customer.salary < 100)"
            + " OR (customer.address != null && customer.address.city == \"Washington\")"
            + " OR (customer.address != null && customer.address.zipCode == 1234)";
    var result = evaluateExpression(expression);
    Assertions.assertTrue(result);
  }

  @Test
  public void evaluateExpressionMultipleOrStatementsFalseTest() throws IOException {
    var expression =
        "(customer.firstName == \"JOHNY\" && customer.salary < 50)"
            + " OR (customer.address != null && customer.address.city == \"Zagreb\")"
            + " OR (customer.address != null && customer.address.zipCode == 10000)";
    var result = evaluateExpression(expression);
    Assertions.assertFalse(result);
  }

  private boolean evaluateExpression(String expression) throws IOException {
    String resourceName = "test/JsonInput.json";

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
    String absolutePath = file.getAbsolutePath();

    var jsonStringInput = Files.readString(Path.of(absolutePath));

    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = null;
    try {
      node = mapper.readTree(jsonStringInput);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    var expressionEntity = Expression.builder().id(1).name("Test").expression(expression).build();

    doReturn(Optional.of(expressionEntity)).when(expressionRepositoryMock).findById(1);

    return expressionEvaluationService.evaluateExpression(1, node);
  }
}
