package exercise.service;

import exercise.domain.ParkingSpot;

@FunctionalInterface
public interface PricingPolicy {

  /**
   * Compute the bill for a car parked in a given spot.
   * @param spot The spot entity.
   * @return The computed amount.
   * @throws EmptyParkingSpotException if no car is parked in the given spot.
   */
  double computeBill(ParkingSpot spot);
}
