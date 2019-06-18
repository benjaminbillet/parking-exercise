package exercise.api;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zalando.problem.Status;

import exercise.App;
import exercise.api.errors.NotFoundException;
import exercise.config.ExceptionTranslator;
import exercise.domain.Car;
import exercise.domain.HourFeePricingType;
import exercise.domain.Parking;
import exercise.domain.ParkingSpotType;
import exercise.dto.BillingDto;
import exercise.dto.ParkingSpotDto;
import exercise.repository.CarRepository;
import exercise.repository.ParkingRepository;
import exercise.repository.ParkingSpotTypeRepository;
import exercise.repository.PricingTypeRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
public class Rfc7807ProblemTest {
  @Autowired
  private ParkingEndpoint parkingEndpoint;
  
  @Autowired
  private ParkingRepository parkingRepository;
  
  @Autowired
  private ParkingSpotTypeRepository spotTypeRepository;

  @Autowired
  private CarRepository carRepository;
  
  @Autowired
  private PricingTypeRepository pricingRepository;
  
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private ExceptionTranslator exceptionTranslator;
  
  @Test
  public void testNotFound() throws Exception {
    getMockEndpoint().perform(post("/api/parking/{parkingId}/spots/byType/{spotType}", 1L, 1L)
        .contentType(TEXT_PLAIN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.entityName", equalTo(ParkingSpotDto.ENTITY_NAME)))
        .andExpect(jsonPath("$.errorKey", equalTo(NotFoundException.ERROR_KEY)))
        .andExpect(jsonPath("$.type", equalTo("https://benjaminbillet.fr/problem/entity-not-found")))
        .andExpect(jsonPath("$.title", equalTo("Cannot find a free parking spot")))
        .andExpect(jsonPath("$.status", equalTo(Status.NOT_FOUND.getStatusCode())))
        .andExpect(jsonPath("$.message", equalTo("error.not-found")))
        .andExpect(jsonPath("$.params", equalTo(ParkingSpotDto.ENTITY_NAME)));
    
    getMockEndpoint().perform(delete("/api/parking/{parkingId}/cars/{carId}", 1L, 1L)
        .contentType(TEXT_PLAIN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.entityName", equalTo(BillingDto.ENTITY_NAME)))
        .andExpect(jsonPath("$.errorKey", equalTo(NotFoundException.ERROR_KEY)))
        .andExpect(jsonPath("$.type", equalTo("https://benjaminbillet.fr/problem/entity-not-found")))
        .andExpect(jsonPath("$.title", equalTo("Car registration not found")))
        .andExpect(jsonPath("$.status", equalTo(Status.NOT_FOUND.getStatusCode())))
        .andExpect(jsonPath("$.message", equalTo("error.not-found")))
        .andExpect(jsonPath("$.params", equalTo(BillingDto.ENTITY_NAME)));
  }

  @Test
  public void testWrongType() throws Exception {    
    getMockEndpoint().perform(post("/api/parking/{parkingId}/spots/byType/{spotType}", "null", "null")
        .contentType(TEXT_PLAIN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.type", equalTo("https://benjaminbillet.fr/problem/problem-generic")))
        .andExpect(jsonPath("$.title", equalTo(Status.BAD_REQUEST.getReasonPhrase())))
        .andExpect(jsonPath("$.status", equalTo(Status.BAD_REQUEST.getStatusCode())));
  }

  @Test
  public void testIllegalState() throws Exception {    
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
    spotType = spotTypeRepository.save(spotType);
    
    Car car = new Car();
    long carId = carRepository.save(car).getId();
    
    getMockEndpoint().perform(delete("/api/parking/{parkingId}/cars/{carId}", parkingId, carId)
        .contentType(TEXT_PLAIN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.type", equalTo("https://benjaminbillet.fr/problem/problem-generic")))
        .andExpect(jsonPath("$.title", equalTo("No parking spot associated to this car")))
        .andExpect(jsonPath("$.status", equalTo(Status.BAD_REQUEST.getStatusCode())));
  }

  private MockMvc getMockEndpoint() {
    return MockMvcBuilders.standaloneSetup(parkingEndpoint)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }
}
