package hr.solujic.leapwise.services;

import hr.solujic.leapwise.entity.Expression;
import hr.solujic.leapwise.model.ExpressionModel;
import hr.solujic.leapwise.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpressionService {

  @Autowired private ExpressionRepository expressionRepository;

  public ExpressionModel createExpression(ExpressionModel expressionModel) {

    var entity =
        expressionRepository.save(
            Expression.builder()
                .expression(expressionModel.getExpression())
                .name(expressionModel.getName())
                .build());

    return ExpressionModel.builder()
        .id(entity.getId())
        .name(entity.getName())
        .expression(entity.getExpression())
        .build();
  }

  public List<ExpressionModel> getAllExpressions() {
    return expressionRepository.findAll().stream()
        .map(
            expression ->
                ExpressionModel.builder()
                    .id(expression.getId())
                    .name(expression.getName())
                    .expression(expression.getExpression())
                    .build())
        .collect(Collectors.toList());
  }
}
