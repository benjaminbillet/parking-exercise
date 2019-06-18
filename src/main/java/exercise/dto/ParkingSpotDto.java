package exercise.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class ParkingSpotDto {
  
  public static final String ENTITY_NAME = "parking-spot";
  
  private Long spotId;
  
  private String spotLabel;
  
  private Long carId;

  public ParkingSpotDto(@NotNull Long spotId, @NotNull String spotLabel, @NotNull Long carId) {
    this.spotId = spotId;
    this.spotLabel = spotLabel;
    this.carId = carId;
  }

  public ParkingSpotDto() {
  }

  public Long getSpotId() {
    return spotId;
  }

  public void setSpotId(Long spotId) {
    this.spotId = spotId;
  }

  public String getSpotLabel() {
    return spotLabel;
  }

  public void setSpotLabel(String spotLabel) {
    this.spotLabel = spotLabel;
  }

  public Long getCarId() {
    return carId;
  }

  public void setCarId(Long carId) {
    this.carId = carId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(carId, spotId, spotLabel);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ParkingSpotDto)) {
      return false;
    }
    ParkingSpotDto other = (ParkingSpotDto)obj;
    return Objects.equals(carId, other.carId) && Objects.equals(spotId, other.spotId)
        && Objects.equals(spotLabel, other.spotLabel);
  }

  @Override
  public String toString() {
    return "ParkingSpotDto [spotId=" + spotId + ", spotLabel=" + spotLabel + ", carId=" + carId + "]";
  }
}
