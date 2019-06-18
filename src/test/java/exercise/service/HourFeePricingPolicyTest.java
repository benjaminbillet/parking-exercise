package exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import exercise.domain.Car;
import exercise.domain.ParkingSpot;

public class HourFeePricingPolicyTest {

  @Test
  public void testHourFeeOnly() {
    HourFeePricingPolicy policy = new HourFeePricingPolicy(2.5);
    ParkingSpot spot = new ParkingSpot();
    spot.setOccupiedBy(new Car());

    spot.setOccupiedSince(Instant.now().minus(90, ChronoUnit.MINUTES));
    assertEquals(2.5, policy.computeBill(spot), 0.1);
    
    spot.setOccupiedSince(Instant.now().minus(2, ChronoUnit.HOURS));
    
    assertEquals(5, policy.computeBill(spot), 0.1);
  }
  
  @Test
  public void testHourAndFixedFee() {
    HourFeePricingPolicy policy = new HourFeePricingPolicy(1.5, 2.5);
    ParkingSpot spot = new ParkingSpot();
    spot.setOccupiedBy(new Car());

    spot.setOccupiedSince(Instant.now().minus(2, ChronoUnit.HOURS));
    assertEquals(6.5, policy.computeBill(spot), 0.1);
  }
  
  @Test
  public void testNegativeValues() {
    assertThrows(IllegalArgumentException.class, () -> new HourFeePricingPolicy(-2.5));
    assertThrows(IllegalArgumentException.class, () -> new HourFeePricingPolicy(-1.5, -2.5));
  }
  
  @Test
  public void testNull() {
    assertThrows(NullPointerException.class, () -> new HourFeePricingPolicy((Double) null));
    assertThrows(NullPointerException.class, () -> new HourFeePricingPolicy(null, null));
  }
  
  @Test
  public void testWrongDates() {
    HourFeePricingPolicy policy = new HourFeePricingPolicy(1.5, 2.5);
    ParkingSpot spot = new ParkingSpot();
    spot.setOccupiedBy(new Car());

    spot.setOccupiedSince(Instant.now().plus(2, ChronoUnit.HOURS));
    assertThrows(IllegalStateException.class, () -> policy.computeBill(spot));
  }
}
