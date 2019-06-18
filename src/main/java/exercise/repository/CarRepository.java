package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import exercise.domain.Car;


@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
