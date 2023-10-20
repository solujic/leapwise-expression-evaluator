package hr.solujic.leapwise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExpressionModel {
  private int id;
  private String expression;
  private String name;
}
