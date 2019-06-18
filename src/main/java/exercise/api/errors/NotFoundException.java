package exercise.api.errors;

import java.net.URI;

import org.zalando.problem.Status;

import exercise.config.ApplicationProperties;

public class NotFoundException extends AbstractException {

  private static final long serialVersionUID = -8659688443627971341L;
  
  public static final String ERROR_KEY = "not-found";

  public NotFoundException(ApplicationProperties config, String title, String entityName, String errorKey) {
    this(config.getRfc7807().getEntityNotFoundTypeUri(), title, entityName, errorKey);
  }
  
  public NotFoundException(ApplicationProperties config, String title, String entityName) {
    this(config, title, entityName, ERROR_KEY);
  }

  public NotFoundException(URI type, String title, String entityName, String errorKey) {
    super(type, title, Status.NOT_FOUND, entityName, errorKey);
  }
}
