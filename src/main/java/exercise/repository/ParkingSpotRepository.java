package exercise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import exercise.domain.Parking;
import exercise.domain.ParkingSpot;
import exercise.domain.ParkingSpotType;


@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
  default Optional<ParkingSpot> findFreeSpot(Parking parking, ParkingSpotType spotType) {
    return findOneByParkingAndTypeAndOccupiedByIsNull(parking, spotType);
  }

  Optional<ParkingSpot> findOneByParkingAndTypeAndOccupiedByIsNull(Parking parking, ParkingSpotType spotType);
}
