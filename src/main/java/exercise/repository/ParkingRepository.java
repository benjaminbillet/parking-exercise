package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import exercise.domain.Parking;


@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
}
