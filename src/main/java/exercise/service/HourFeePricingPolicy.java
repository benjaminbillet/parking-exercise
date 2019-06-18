package exercise.service;

import java.time.Duration;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import exercise.domain.HourFeePricingType;
import exercise.domain.ParkingSpot;
import exercise.domain.PricingType;

/**
 * Hour-based pricing policy .
 */
public class HourFeePricingPolicy implements PricingPolicy {

  @NotNull
  private Double minimumFee;

  @NotNull
  private Double hourFee;
  
  HourFeePricingPolicy(PricingType abstractType) {
    HourFeePricingType type = (HourFeePricingType) abstractType;
    this.minimumFee = type.getMinimumFee();
    this.hourFee = type.getHourFee();
  }
  
  public HourFeePricingPolicy(Double hourFee) {
    this(0D, hourFee);
  }

  /**
   * @param minimumFee Minimum amount charged.
   * @param hourFee Hour fee.
   */
  public HourFeePricingPolicy(Double minimumFee, Double hourFee) {
    if (minimumFee < 0 || hourFee < 0) {
      throw new IllegalArgumentException("Fees must be positive or zero");
    }
    this.minimumFee = minimumFee;
    this.hourFee = hourFee;
  }

  @Override
  public double computeBill(ParkingSpot spot) {
    if (spot.getOccupiedSince() == null || spot.getOccupiedBy() == null) {
      throw new EmptyParkingSpotException();
    }
    Duration billedDuration = Duration.between(spot.getOccupiedSince(), Instant.now());
    
    // with this strategy, we consider that only complete hours are billed (floor strategy)
    long hours = billedDuration.toHours();
    // an alternate strategy would be to always bill full hours (ceil strategy)
    // long hours = (long) Math.ceil((double) billedDuration.toMinutes() / 60D); // NOSONAR

    if (hours < 0) {
      throw new IllegalStateException("Occupation time is negative");
    }
    
    return minimumFee + hourFee * hours;
  }
}
