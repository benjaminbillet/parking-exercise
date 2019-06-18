package exercise.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import exercise.domain.ParkingSpot;
import exercise.repository.ParkingSpotRepository;

@Service
@Transactional
public class BillingService {

  private final PricingPolicyFactory policyFactory;

  private final ParkingSpotRepository spotRepository;
  
  public BillingService(ParkingSpotRepository spotRepository, PricingPolicyFactory policyFactory) {
    this.spotRepository = spotRepository;
    this.policyFactory = policyFactory;
  }

  /**
   * Compute the bill for a car parked in a given spot.
   * @param spotId The spot id.
   * @return The computed amount.
   */
  @Transactional(readOnly = true)
  public Optional<Double> computeBill(Long spotId) {
    Optional<ParkingSpot> result = spotRepository.findById(spotId);
    return result.map(this::computeBill);
  }
  
  /**
   * Compute the bill for a car parked in a given spot.
   * @param spot The spot entity.
   * @return The computed amount.
   */
  @Transactional(readOnly = true)
  public double computeBill(ParkingSpot spot) {
    PricingPolicy policy = policyFactory.build(spot.getParking().getPricing());
    return policy.computeBill(spot);
  }
}
