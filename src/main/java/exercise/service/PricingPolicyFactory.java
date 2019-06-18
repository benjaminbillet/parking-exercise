package exercise.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import exercise.domain.HourFeePricingType;
import exercise.domain.PricingType;

/**
 * A factory for building pricing policy implementations.
 */
@Service
public class PricingPolicyFactory {
  private final Map<Class<? extends PricingType>, PricingPolicyBuilder> builders = new HashMap<>();
  
  public PricingPolicyFactory() {
    builders.put(HourFeePricingType.class, HourFeePricingPolicy::new);
  }
  
  public PricingPolicy build(PricingType type) {
    return Optional.ofNullable(builders.get(type.getClass()))
        .map(builder -> builder.build(type))
        .orElseThrow(PricingPolicyNotFoundException::new);
  }
}
