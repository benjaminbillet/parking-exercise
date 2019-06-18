package exercise.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import exercise.App;
import exercise.api.errors.NotFoundException;
import exercise.config.ExceptionTranslator;
import exercise.domain.Car;
import exercise.domain.HourFeePricingType;
import exercise.domain.Parking;
import exercise.domain.ParkingSpot;
import exercise.domain.ParkingSpotType;
import exercise.repository.CarRepository;
import exercise.repository.ParkingRepository;
import exercise.repository.ParkingSpotRepository;
import exercise.repository.ParkingSpotTypeRepository;
import exercise.repository.PricingTypeRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
public class ParkingEndpointTest {
  @Autowired
  private ParkingEndpoint parkingEndpoint;
  
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
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private ExceptionTranslator exceptionTranslator;


  @Test
  public void testCheckin() throws Exception {
    HourFeePricingType type = new HourFeePricingType();
    type.setHourFee(1.5);
    type.setMinimumFee(0D);
    type = pricingRepository.save(type);

    Parking parking = new Parking();
    parking.setName("test-parking");
    parking.setPricing(type);
    parking = parkingRepository.save(parking);
    
    ParkingSpotType spotType = new ParkingSpotType();
    spotType.setLabel("spot-type");
    spotType = spotTypeRepository.save(spotType);
    
    ParkingSpot spot = new ParkingSpot();
    spot.setLabel("1A");
    spot.setParking(parking);
    spot.setType(spotType);
    spot = spotRepository.save(spot);

    getMockEndpoint().perform(post("/api/parking/{parkingId}/spots/byType/{spotType}", parking.getId(), spotType.getId())
        .contentType(TEXT_PLAIN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.spotId", equalTo(spot.getId().intValue())))
        .andExpect(jsonPath("$.spotLabel", equalTo(spot.getLabel())))
        .andExpect(jsonPath("$.carId", not(isEmptyString())));
  }
  
  @Test
  public void testCheckout() throws Exception {
    HourFeePricingType type = new HourFeePricingType();
    type.setHourFee(1.5);
    type.setMinimumFee(0D);
    type = pricingRepository.save(type);

    Parking parking = new Parking();
    parking.setName("test-parking");
    parking.setPricing(type);
    parking = parkingRepository.save(parking);
    
    ParkingSpotType spotType = new ParkingSpotType();
    spotType.setLabel("spot-type");
    spotType = spotTypeRepository.save(spotType);
    
    Car car = new Car();
    car = carRepository.save(car);
    
    ParkingSpot spot = new ParkingSpot();
    spot.setLabel("1A");
    spot.setParking(parking);
    spot.setType(spotType);
    spot.setOccupiedBy(car);
    spot.setOccupiedSince(Instant.now().minus(90, ChronoUnit.MINUTES));
    spot = spotRepository.save(spot);

    getMockEndpoint().perform(delete("/api/parking/{parkingId}/cars/{carId}", parking.getId(), car.getId())
        .contentType(TEXT_PLAIN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.charged", equalTo(1.5)))
        .andExpect(jsonPath("$.time", not(isEmptyString())));
  }
  
  @Test
  public void testCheckinNotFound() {
    HourFeePricingType type = new HourFeePricingType();
    type.setHourFee(1.5);
    type.setMinimumFee(0D);
    type = pricingRepository.save(type);

    Parking parking = new Parking();
    parking.setName("test-parking");
    parking.setPricing(type);
    long parkingId = parkingRepository.save(parking).getId();
    
    ParkingSpotType spotType = new ParkingSpotType();
    spotType.setLabel("spot-type");
    long typeId = spotTypeRepository.save(spotType).getId();
    
    assertThrows(NotFoundException.class, () -> parkingEndpoint.checkin(parkingId, typeId));
    assertThrows(NotFoundException.class, () -> parkingEndpoint.checkin(parkingId + 1, typeId));
    assertThrows(NotFoundException.class, () -> parkingEndpoint.checkin(parkingId, typeId + 1));
    assertThrows(NotFoundException.class, () -> parkingEndpoint.checkin(-1L, -1L));
  }
  
  private MockMvc getMockEndpoint() {
    return MockMvcBuilders.standaloneSetup(parkingEndpoint)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }
}
