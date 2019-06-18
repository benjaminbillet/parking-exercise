package exercise.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import exercise.domain.ParkingSpot;
import exercise.repository.CarRepository;

@Mapper(componentModel = "spring", uses = { CarRepository.class })
public interface ParkingSpotMapper extends EntityMapper<ParkingSpotDto, ParkingSpot> {
  @Mapping(source = "occupiedBy.id", target = "carId")
  @Mapping(source = "label", target = "spotLabel")
  @Mapping(source = "id", target = "spotId")
  ParkingSpotDto toDto(ParkingSpot resource);

  @Mapping(source = "spotId", target = "id")
  @Mapping(source = "spotLabel", target = "label")
  @Mapping(source = "carId", target = "occupiedBy")
  @Mapping(target = "occupiedSince", ignore = true)
  @Mapping(target = "parking", ignore = true)
  @Mapping(target = "type", ignore = true)
  ParkingSpot toEntity(ParkingSpotDto dto);
}
