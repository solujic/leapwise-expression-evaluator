package hr.solujic.leapwise.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ExpressionRequestDto implements Serializable {

  @Schema(
      example =
          "(customer.firstName == \"JOHN\" && customer.salary < 100)"
              + " OR (customer.address != null && customer.address.city == \"Washington\")")
  @NotNull(message = "Request body field 'expression' can not be null!")
  private String expression;

  @Schema(example = "Complex logical expression")
  @NotNull(message = "Request body field 'name' can not be null!")
  private String name;
}
