package exercise.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import exercise.domain.HourFeePricingType;
import exercise.domain.PricingType;

/**
 * A factory for building pricing policy implementations.
 * @see PricingPolicy
 */
@Service
public class PricingPolicyFactory {
  private final Map<Class<? extends PricingType>, PricingPolicyBuilder> builders = new HashMap<>();
  
  public PricingPolicyFactory() {
    builders.put(HourFeePricingType.class, HourFeePricingPolicy::new);
  }
  
  /**
   * Build a new policy based on the concrete type of a pricing entity.
   * @param type The pricing type
   * @return A pricing policy implementation.
   * @throws PricingPolicyNotFoundException if no pricing policy implementation matches the entity type.
   */
  public PricingPolicy build(PricingType type) {
    return Optional.ofNullable(builders.get(type.getClass()))
        .map(builder -> builder.build(type))
        .orElseThrow(PricingPolicyNotFoundException::new);
  }
}
