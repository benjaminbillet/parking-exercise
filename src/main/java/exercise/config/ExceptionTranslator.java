package exercise.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

// this class parameterize various behaviors of rest controllers (exception handling, security, validation, etc.)
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

  private ApplicationProperties config;

  public ExceptionTranslator(ApplicationProperties config) {
    this.config = config;
  }

  // process the RFC7807 problem payload
  @Override
  public ResponseEntity<Problem> process(ResponseEntity<Problem> entity, NativeWebRequest request) {
    if (entity == null) {
      return entity;
    }

    Problem problem = entity.getBody();
    if (!(problem instanceof DefaultProblem)) {
      return entity;
    }

    ProblemBuilder builder = Problem.builder()
        .withStatus(problem.getStatus())
        .withTitle(problem.getTitle())
        .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());
    if (Problem.DEFAULT_TYPE.equals(problem.getType())) {
      builder.withType(config.getRfc7807().getDefaultTypeUri());
    } else {
      builder.withType(problem.getType());
    }

    return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
  }
  
  @ExceptionHandler
  public ResponseEntity<Problem> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex, NativeWebRequest request) {
    return handleBadRequest(ex, request);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleIllegalStateException(IllegalStateException ex, NativeWebRequest request) {
    return handleBadRequest(ex, request);
  }
  
  @ExceptionHandler
  public ResponseEntity<Problem> handleIllegalArgumentException(IllegalArgumentException ex, NativeWebRequest request) {
    return handleBadRequest(ex, request);
  }
  
  private <T extends Exception> ResponseEntity<Problem> handleBadRequest(T ex, NativeWebRequest request) {
    Problem problem = Problem.builder()
        .withStatus(Status.BAD_REQUEST)
        .withTitle(ex.getMessage())
        .with("message", config.getRfc7807().getDefaultTypeUri()).build();
    return create(ex, problem, request);
  }
}
