package exercise.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "parking_spot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ParkingSpot implements Serializable {

  private static final long serialVersionUID = -1355208253254485400L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull
  @Column(name = "label")
  private String label;

  @Column(name = "occupied_since")
  private Instant occupiedSince;

  @OneToOne
  @JoinColumn
  private Car occupiedBy;
  
  @NotNull
  @ManyToOne
  @JoinColumn
  private Parking parking;

  @NotNull
  @ManyToOne
  @JoinColumn
  private ParkingSpotType type;

  public ParkingSpot(Long id, String label, Instant occupiedSince, Car occupiedBy, Parking parking,
      ParkingSpotType type) {
    this.id = id;
    this.label = label;
    this.occupiedSince = occupiedSince;
    this.occupiedBy = occupiedBy;
    this.parking = parking;
    this.type = type;
  }

  public ParkingSpot() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Instant getOccupiedSince() {
    return occupiedSince;
  }

  public void setOccupiedSince(Instant occupiedSince) {
    this.occupiedSince = occupiedSince;
  }

  public Car getOccupiedBy() {
    return occupiedBy;
  }

  public void setOccupiedBy(Car occupiedBy) {
    this.occupiedBy = occupiedBy;
  }

  public Parking getParking() {
    return parking;
  }

  public void setParking(Parking parking) {
    this.parking = parking;
  }

  public ParkingSpotType getType() {
    return type;
  }

  public void setType(ParkingSpotType type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, label, occupiedBy, occupiedSince, parking, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ParkingSpot)) {
      return false;
    }
    ParkingSpot other = (ParkingSpot)obj;
    return Objects.equals(id, other.id) && Objects.equals(label, other.label)
        && Objects.equals(occupiedBy, other.occupiedBy) && Objects.equals(occupiedSince, other.occupiedSince)
        && Objects.equals(parking, other.parking) && Objects.equals(type, other.type);
  }

  @Override
  public String toString() {
    return "ParkingSpot [id=" + id + ", label=" + label + ", occupiedSince=" + occupiedSince + ", occupiedBy="
        + Optional.of(occupiedBy).map(Car::getId).orElse(null) + ", parking=" + parking 
        + ", type=" + type + "]";
  }
}
