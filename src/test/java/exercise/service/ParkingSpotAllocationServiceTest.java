package exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import exercise.App;
import exercise.domain.Car;
import exercise.domain.HourFeePricingType;
import exercise.domain.Parking;
import exercise.domain.ParkingSpot;
import exercise.domain.ParkingSpotType;
import exercise.dto.BillingDto;
import exercise.dto.ParkingSpotDto;
import exercise.repository.CarRepository;
import exercise.repository.ParkingRepository;
import exercise.repository.ParkingSpotRepository;
import exercise.repository.ParkingSpotTypeRepository;
import exercise.repository.PricingTypeRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingSpotAllocationServiceTest {

  @Autowired
  private ParkingRepository parkingRepository;

  @Autowired
  private ParkingSpotTypeRepository spotTypeRepository;

  @Autowired
  private ParkingSpotRepository spotRepository;

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private PricingTypeRepository pricingRepository;

  @Autowired
  private ParkingSpotAllocationService allocationService;


  @Test
  public void testCheckin() {
    ParkingSpot spot = makeFakeParking(false);
    long parkingId = spot.getParking().getId();
    long typeId = spot.getType().getId();

    ParkingSpotDto foundSpot = allocationService.checkin(parkingId, typeId).get();
    assertEquals(spot.getId(), foundSpot.getSpotId());

    spot = spotRepository.findById(foundSpot.getSpotId()).get();
    assertNotNull(spot);
    assertNotNull(spot.getOccupiedSince());
    assertNotNull(spot.getOccupiedBy());
    assertEquals(spot.getOccupiedBy().getId(), foundSpot.getCarId());
    assertEquals(spot.getOccupiedBy().getOccupiedSpot().getId(), foundSpot.getSpotId());
    assertEquals(spot.getLabel(), foundSpot.getSpotLabel());
  }

  @Test
  public void testCheckinFullParking() {
    ParkingSpot spot = makeFakeParking(true);
    long parkingId = spot.getParking().getId();
    long typeId = spot.getType().getId();

    Optional<ParkingSpotDto> foundSpot = allocationService.checkin(parkingId, typeId);
    assertTrue(foundSpot.isEmpty());
  }

  @Test
  public void testCheckinWrongParking() {
    ParkingSpot spot = makeFakeParking(false);
    long parkingId = spot.getParking().getId();
    long typeId = spot.getType().getId();

    Optional<ParkingSpotDto> foundSpot = allocationService.checkin(parkingId + 1, typeId);
    assertTrue(foundSpot.isEmpty());
  }

  @Test
  public void testCheckout() {
    ParkingSpot spot = makeFakeParking(true);
    long carId = spot.getOccupiedBy().getId();
    long parkingId = spot.getParking().getId();
    
    BillingDto billing = allocationService.checkout(parkingId, carId).get();
    assertEquals(1.5, billing.getCharged(), 0.1);

    spot = spotRepository.findById(spot.getId()).get();
    assertNotNull(spot);
    assertNull(spot.getOccupiedBy());
    assertNull(spot.getOccupiedSince());

    assertTrue(carRepository.findById(carId).isEmpty());
  }

  @Test
  public void testCheckoutNotFound() {
    ParkingSpot spot = makeFakeParking(true);
    long carId = spot.getOccupiedBy().getId();
    long parkingId = spot.getParking().getId();

    Optional<BillingDto> billing = allocationService.checkout(parkingId, carId + 1);
    assertTrue(billing.isEmpty());
  }
  
  @Test
  public void testCheckoutInvalidValues() {
    ParkingSpot spot = makeFakeParking(true);
    long parkingId = spot.getParking().getId();
    long carId = spot.getOccupiedBy().getId();

    assertTrue(allocationService.checkout(-1L, -1L).isEmpty());
    assertTrue(allocationService.checkout(parkingId, -1L).isEmpty());
    
    assertThrows(IllegalStateException.class, () -> allocationService.checkout(-1L, carId));

    assertThrows(InvalidDataAccessApiUsageException.class, () -> allocationService.checkout(parkingId, null));
    assertThrows(InvalidDataAccessApiUsageException.class, () -> allocationService.checkout(null, carId));
    assertThrows(InvalidDataAccessApiUsageException.class, () -> allocationService.checkout(null, null));
  }
  
  private ParkingSpot makeFakeParking(boolean occupied) {
    HourFeePricingType type = new HourFeePricingType();
    type.setHourFee(1.5);
    type.setMinimumFee(0D);
    type = pricingRepository.save(type);

    Parking parking = new Parking();
    parking.setName("test-parking");
    parking.setPricing(type);
    parking = parkingRepository.save(parking);

    ParkingSpotType spotType = new ParkingSpotType();
    spotType.setLabel("electric-50kWh");
    spotType = spotTypeRepository.save(spotType);

    ParkingSpot spot = new ParkingSpot();
    spot.setLabel("1A");
    spot.setParking(parking);
    spot.setType(spotType);

    if (occupied) {
      Car car = new Car();
      car = carRepository.save(car);
      
      spot.setOccupiedBy(car);
      spot.setOccupiedSince(Instant.now().minus(90, ChronoUnit.MINUTES));
    }
    
    return spotRepository.save(spot);
  }
}
