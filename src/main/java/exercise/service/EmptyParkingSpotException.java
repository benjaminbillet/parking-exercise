package exercise.service;

public class EmptyParkingSpotException extends RuntimeException {

  private static final long serialVersionUID = -5131357518481096740L;

  public EmptyParkingSpotException() {
  }

  public EmptyParkingSpotException(String message) {
    super(message);
  }
}
