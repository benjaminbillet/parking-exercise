package exercise.service;

import exercise.domain.PricingType;

@FunctionalInterface
public interface PricingPolicyBuilder {
  PricingPolicy build(PricingType type);
}
