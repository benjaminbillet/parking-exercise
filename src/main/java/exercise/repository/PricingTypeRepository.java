package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import exercise.domain.PricingType;


@Repository
public interface PricingTypeRepository extends JpaRepository<PricingType, Long> {
}
