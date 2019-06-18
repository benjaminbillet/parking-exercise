package exercise.api;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import exercise.api.errors.NotFoundException;
import exercise.config.ApplicationProperties;
import exercise.dto.BillingDto;
import exercise.dto.ParkingSpotDto;
import exercise.service.ParkingSpotAllocationService;

@RestController
@RequestMapping("/api/parking")
public class ParkingEndpoint {
  
  private ParkingSpotAllocationService allocationService;

  private ApplicationProperties config;

  public ParkingEndpoint(ParkingSpotAllocationService allocationService, ApplicationProperties config) {
    this.allocationService = allocationService;
    this.config = config;
  }

  @PostMapping("/{parkingId}/spots/byType/{spotType}")
  public ResponseEntity<ParkingSpotDto> checkin(@PathVariable Long parkingId, @PathVariable Long spotType) {
    Optional<ParkingSpotDto> dto = allocationService.checkin(parkingId, spotType);
    return ResponseEntity.ok(dto.orElseThrow(
        () -> new NotFoundException(config, "Cannot find a free parking spot", ParkingSpotDto.ENTITY_NAME, "not-found")));
  }

  @DeleteMapping("/{parkingId}/cars/{carId}")
  public ResponseEntity<BillingDto> checkout(@PathVariable Long parkingId, @PathVariable Long carId) {
    Optional<BillingDto> dto = allocationService.checkout(parkingId, carId);
    return ResponseEntity.ok(dto.orElseThrow(
        () -> new NotFoundException(config, "Car registration not found", BillingDto.ENTITY_NAME, "not-found")));
  }
}
