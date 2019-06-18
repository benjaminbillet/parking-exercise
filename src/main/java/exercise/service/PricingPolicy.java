package exercise.service;

import exercise.domain.ParkingSpot;

@FunctionalInterface
public interface PricingPolicy {
  double computeBill(ParkingSpot spot);
}
