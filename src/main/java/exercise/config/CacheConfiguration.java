package exercise.config;

import java.time.Duration;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import exercise.domain.Car;
import exercise.domain.HourFeePricingType;
import exercise.domain.Parking;
import exercise.domain.ParkingSpot;
import exercise.domain.ParkingSpotType;
import exercise.domain.PricingType;

@Configuration
@EnableCaching
public class CacheConfiguration {
  private final javax.cache.configuration.Configuration<Object, Object> cacheConfiguration;

  public CacheConfiguration() {
    this.cacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder
        .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(1000))
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(3600)))
        .build());
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      // cache for simple entities
      cm.createCache(Parking.class.getName(), cacheConfiguration);
      cm.createCache(ParkingSpot.class.getName(), cacheConfiguration);
      cm.createCache(ParkingSpotType.class.getName(), cacheConfiguration);
      cm.createCache(Car.class.getName(), cacheConfiguration);
      cm.createCache(PricingType.class.getName(), cacheConfiguration);
      cm.createCache(HourFeePricingType.class.getName(), cacheConfiguration);
    };
  }
}
