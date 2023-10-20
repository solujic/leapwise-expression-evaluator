package hr.solujic.leapwise.dto.request;

import java.io.Serializable;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EvaluateRequestDto implements Serializable {

  @NotNull(message = "Request body field 'id' can not be null!")
  private int id;

  @Schema(
      example =
          """
      {
        "customer":
        {
          "firstName": "JOHN",
          "lastName": "DOE",\s
          "address":
          {
            "city": "Chicago",
            "zipCode": 1234,\s
            "street": "56th",\s
            "houseNumber": 2345
          },
          "salary": 99,
          "type": "BUSINESS"
        }
      }
      """)
  @NotNull(message = "Request body field 'jsonObject' can not be null!")
  private JsonNode jsonObject;
}
