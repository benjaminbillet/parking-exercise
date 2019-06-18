package exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import exercise.App;
import exercise.domain.Car;
import exercise.domain.HourFeePricingType;
import exercise.domain.Parking;
import exercise.domain.ParkingSpot;
import exercise.domain.PricingType;
import exercise.repository.ParkingSpotRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
public class BillingServiceTest {
  @Mock
  private ParkingSpotRepository spotRepository;

  @Test
  public void testComputeBill() {
    ParkingSpot spot = makeFakeSpot(1.5, Instant.now().minus(1, ChronoUnit.HOURS));
    when(spotRepository.findById(any())).thenReturn(Optional.of(spot));

    BillingService billingService = new BillingService(spotRepository, new PricingPolicyFactory());

    Optional<Double> charged = billingService.computeBill(spot.getId());
    assertEquals(1.5, charged.get(), 0.1);
  }

  @Test
  public void testInexistantSpot() {
    BillingService billingService = new BillingService(spotRepository, new PricingPolicyFactory());
    Optional<Double> charged = billingService.computeBill(10L);
    assertTrue(charged.isEmpty());
  }

  @Test
  public void testEmptySpot() {
    ParkingSpot spot = makeFakeSpot(1.5, Instant.now().minus(1, ChronoUnit.HOURS));
    spot.setOccupiedBy(null);
    spot.setOccupiedSince(null);
    when(spotRepository.findById(any())).thenReturn(Optional.of(spot));

    BillingService billingService = new BillingService(spotRepository, new PricingPolicyFactory());
    assertThrows(EmptyParkingSpotException.class, () -> billingService.computeBill(10L));
  }

  @Test
  public void testInexistantPolicy() {
    ParkingSpot spot = makeFakeSpot(1.5, Instant.now().minus(1, ChronoUnit.HOURS));
    spot.getParking().setPricing(new UnknownPricingType());
    when(spotRepository.findById(any())).thenReturn(Optional.of(spot));

    BillingService billingService = new BillingService(spotRepository, new PricingPolicyFactory());
    assertThrows(PricingPolicyNotFoundException.class, () -> billingService.computeBill(spot.getId()));
  }

  private ParkingSpot makeFakeSpot(double hourFee, Instant occupiedSince) {
    HourFeePricingType type = new HourFeePricingType();
    type.setHourFee(hourFee);
    type.setMinimumFee(0D);

    Parking parking = new Parking();
    parking.setPricing(type);

    ParkingSpot spot = new ParkingSpot();
    spot.setId(1L);
    spot.setLabel("1A");
    spot.setOccupiedSince(occupiedSince);
    spot.setOccupiedBy(new Car());
    spot.setParking(parking);

    return spot;
  }

  private class UnknownPricingType extends PricingType {
    private static final long serialVersionUID = -982147880899041980L;
  }
}
