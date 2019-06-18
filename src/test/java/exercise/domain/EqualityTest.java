package exercise.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Instant;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import exercise.dto.BillingDto;
import exercise.dto.ParkingSpotDto;

public class EqualityTest {

  @Test
  public void parkingSpotEntityTest() {
    Parking parking = new Parking(1L, "test-parking", new HourFeePricingType(1L, 0D, 1.5));
    ParkingSpotType type = new ParkingSpotType(1L, "test-type");
    testEquality(
        () -> new ParkingSpot(1L, "P1", null, null, parking, type),
        () -> new ParkingSpot(2L, "P2", null, null, parking, type));
  }

  @Test
  public void parkingSpotTypeEntityTest() {
    testEquality(
        () -> new ParkingSpotType(1L, "test-type"),
        () -> new ParkingSpotType(1L, "another-type"));
  }

  @Test
  public void carEntityTest() {
    testEquality(
        () -> new Car(1L, null),
        () -> new Car(2L, null));
   }

  @Test
  public void parkingEntityTest() {
    testEquality(
        () -> new Parking(1L, "test-parking", new HourFeePricingType(1L, 0D, 1.5)),
        () -> new Parking(2L, "another-parking", new HourFeePricingType(2L, 0D, 2D)));
   }

  @Test
  public void pricingTypeEntityTest() {
    testEquality(
        () -> new HourFeePricingType(1L, 0D, 1.5),
        () -> new HourFeePricingType(2L, 1D, 2D));

    testEquality(
        () -> new HourFeePricingType(1L, 0D, 1.5),
        () -> new UnknownPricingType());
  }

  @Test
  public void billingDtoTest() {
    Instant now = Instant.now();
    testEquality(
        () -> new BillingDto(1.5, now),
        () -> new BillingDto(2.5, now));
   }

  @Test
  public void parkingSpotDtoTest() {
    testEquality(
        () -> new ParkingSpotDto(1L, "1A", 2L),
        () -> new ParkingSpotDto(2L, "1B", 1L));
  }

  private <T> void testEquality(Supplier<T> supplier1, Supplier<T> supplier2) {
    // test that objets are equals to themselves
    T obj = supplier1.get();
    assertEquals(obj.hashCode(), obj.hashCode());
    assertEquals(obj, obj);

    // test that two identical objects are equals
    assertEquals(supplier1.get().hashCode(), supplier1.get().hashCode());
    assertEquals(supplier1.get(), supplier1.get());

    assertEquals(supplier2.get().hashCode(), supplier2.get().hashCode());
    assertEquals(supplier2.get(), supplier2.get());

    // test that two different objects are not equals, symmetrically
    assertNotEquals(supplier1.get(), supplier2.get());
    assertNotEquals(supplier2.get(), supplier1.get());

    // test that the supplied objects are different from other objects
    assertNotEquals(supplier1.get(), new Object());
    assertNotEquals(supplier2.get(), new Object());

    // test that the supplied objects are different from null
    assertNotEquals(supplier1.get(), null); // NOSONAR
    assertNotEquals(supplier2.get(), null); // NOSONAR
  }

  private class UnknownPricingType extends PricingType {
    private static final long serialVersionUID = -982147880899041980L;
  }
}
