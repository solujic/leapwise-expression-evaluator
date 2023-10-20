package hr.solujic.leapwise.controller;

import hr.solujic.leapwise.dto.request.EvaluateRequestDto;
import hr.solujic.leapwise.dto.request.ExpressionRequestDto;
import hr.solujic.leapwise.dto.response.ExpressionResponseDto;
import hr.solujic.leapwise.model.ExpressionModel;
import hr.solujic.leapwise.services.ExpressionEvaluationService;
import hr.solujic.leapwise.services.ExpressionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Logical Expression API")
@Validated
@RestController
public class ExpressionController {

  @Autowired ExpressionService expressionService;
  @Autowired ExpressionEvaluationService expressionEvaluationService;

  @Operation(summary = "Create a new logical expression.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Expression successfully created.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ExpressionResponseDto.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Request body validation failed.",
            content = @Content)
      })
  @PostMapping("/expression")
  ResponseEntity<ExpressionResponseDto> createExpression(
      @Valid @RequestBody ExpressionRequestDto expressionDto) {
    var response =
        expressionService.createExpression(
            ExpressionModel.builder()
                .expression(expressionDto.getExpression())
                .name(expressionDto.getName())
                .build());

    return ResponseEntity.ok(
        ExpressionResponseDto.builder()
            .id(response.getId())
            .expression(response.getExpression())
            .name(response.getName())
            .build());
  }

  @Operation(summary = "Get all logical expressions.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fetched the expressions.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ExpressionResponseDto.class))
            })
      })
  @GetMapping("/expression")
  ResponseEntity<List<ExpressionResponseDto>> getExpressions() {

    var expressionModelList = expressionService.getAllExpressions();

    return ResponseEntity.ok(
        expressionModelList.stream()
            .map(
                e ->
                    ExpressionResponseDto.builder()
                        .id(e.getId())
                        .expression(e.getExpression())
                        .name(e.getName())
                        .build())
            .collect(Collectors.toList()));
  }

  @Operation(summary = "Evaluate the logical expression against the passed JSON object.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Expression successfully evaluated.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Boolean.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Request body validation failed.",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "Expression not found.",
            content = @Content)
      })
  @PostMapping("/evaluate")
  ResponseEntity<Boolean> evaluateExpression(
      @Valid @RequestBody EvaluateRequestDto evaluateRequestDto) {
    var response =
        expressionEvaluationService.evaluateExpression(
            evaluateRequestDto.getId(), evaluateRequestDto.getJsonObject());
    return ResponseEntity.ok(response);
  }
}
