package exercise.service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import exercise.domain.Car;
import exercise.domain.Parking;
import exercise.domain.ParkingSpot;
import exercise.domain.ParkingSpotType;
import exercise.dto.BillingDto;
import exercise.dto.ParkingSpotDto;
import exercise.dto.ParkingSpotMapper;
import exercise.repository.CarRepository;
import exercise.repository.ParkingRepository;
import exercise.repository.ParkingSpotRepository;
import exercise.repository.ParkingSpotTypeRepository;

@Service
@Transactional
public class ParkingSpotAllocationService {

  private final Logger log = LoggerFactory.getLogger(ParkingSpotAllocationService.class);

  private final CarRepository carRepository;
  private final ParkingSpotRepository spotRepository;
  private final ParkingSpotMapper spotMapper;
  private final BillingService billingService;
  private final ParkingSpotTypeRepository spotTypeRepository;
  private final ParkingRepository parkingRepository;


  public ParkingSpotAllocationService(ParkingRepository parkingRepository, ParkingSpotRepository spotRepository, ParkingSpotMapper spotMapper, ParkingSpotTypeRepository spotTypeRepository, CarRepository carRepository, BillingService billingService) {
    this.spotRepository = spotRepository;
    this.spotMapper = spotMapper;
    this.carRepository = carRepository;
    this.billingService = billingService;
    this.parkingRepository = parkingRepository;
    this.spotTypeRepository = spotTypeRepository;
  }
  
  /**
   * Look for a free parking spot.
   * @param parkingId The targeted parking
   * @param requestedSpotTypeId The type of parking spot to look for.
   * @return A parking spot DTO.
   */
  public Optional<ParkingSpotDto> checkin(Long parkingId, Long requestedSpotTypeId) {
    try {
      Optional<Parking> parking = parkingRepository.findById(parkingId);
      Optional<ParkingSpotType> type = spotTypeRepository.findById(requestedSpotTypeId);
      return checkin(parking.orElseThrow(), type.orElseThrow());
    } catch (NoSuchElementException e) {
      return Optional.empty();
    }
  }

  /**
   * Look for a free parking spot.
   * @param parking The targeted parking
   * @param requestedSpotType The type of parking spot to look for.
   * @return A parking spot DTO.
   */
  public Optional<ParkingSpotDto> checkin(Parking parking, ParkingSpotType requestedSpotType) {    
    Optional<ParkingSpot> result = spotRepository.findFreeSpot(parking, requestedSpotType);
    return result.map(spot -> {
      Car car = new Car();
      car = carRepository.save(car);

      spot.setOccupiedBy(car);
      spot.setOccupiedSince(Instant.now());
      spotRepository.save(spot);
      
      log.info("checkin - car: {}, parking: {}, spot: {}", car.getId(), parking.getId(), spot.getId());
      return spotMapper.toDto(spot);
    });
  }

  /**
   * Compute the bill for the car parked at a parking spot, and mark the spot as free.
   * @param parkingId The targeted parking
   * @param carId The id of the leaving car
   * @return A billing DTO
   */
  public Optional<BillingDto> checkout(Long parkingId, Long carId) {
    Optional<Car> result = carRepository.findById(carId);
    return result.map(car -> {
      ParkingSpot spot = car.getOccupiedSpot();
      if (parkingId == null) {
        // to be consistent with repository exceptions
        throw new InvalidDataAccessApiUsageException("Parking id is null");
      }
      if (spot == null || !spot.getParking().getId().equals(parkingId)) {
        throw new IllegalStateException("No parking spot associated to this car");
      }

      double charged = billingService.computeBill(spot);
      spot.setOccupiedBy(null);
      spot.setOccupiedSince(null);
      spotRepository.save(spot);

      carRepository.delete(car);
      
      log.info("Charged {}", charged);
      return new BillingDto(charged);
    });
  }
}
