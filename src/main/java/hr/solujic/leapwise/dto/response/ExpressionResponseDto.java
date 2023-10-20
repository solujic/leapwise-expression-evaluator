package hr.solujic.leapwise.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExpressionResponseDto implements Serializable {
  private int id;
  private String expression;
  private String name;
}
