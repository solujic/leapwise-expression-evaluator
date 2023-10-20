package hr.solujic.leapwise.controller;

import hr.solujic.leapwise.exception.ExpressionNotFoundException;
import hr.solujic.leapwise.exception.WrongOperatorUsedInsideExpressionException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  public ProblemDetail onWrongOperatorUsedInsideExpressionException(
      WrongOperatorUsedInsideExpressionException exception) {
    var pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
    pd.setTitle(WrongOperatorUsedInsideExpressionException.class.getCanonicalName());
    return pd;
  }

  @ExceptionHandler
  public ProblemDetail onWrongOperatorUsedInsideExpressionException(
      ExpressionNotFoundException exception) {
    var pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());
    pd.setTitle(ExpressionNotFoundException.class.getCanonicalName());
    return pd;
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    var pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), ex.getMessage());

    List<FieldError> errors = ex.getFieldErrors();
    StringBuilder stringBuilder = new StringBuilder();
    for (FieldError error : errors) {
      stringBuilder
          .append("Field Name: ")
          .append(error.getField())
          .append(".")
          .append(" Error Message: ")
          .append(error.getDefaultMessage())
          .append(" ");
    }

    pd.setDetail(stringBuilder.toString().trim());
    pd.setTitle(MethodArgumentNotValidException.class.getCanonicalName());
    return new ResponseEntity<>(pd, status);
  }
}
