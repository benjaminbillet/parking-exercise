package exercise.service;

public class PricingPolicyNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 7842959065190463789L;

  public PricingPolicyNotFoundException() {
  }

  public PricingPolicyNotFoundException(String message) {
    super(message);
  }
}
