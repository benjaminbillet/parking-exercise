package exercise.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Car implements Serializable {

  private static final long serialVersionUID = 5823146533660148266L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy="occupiedBy")
  @JoinColumn
  private ParkingSpot occupiedSpot;
  
  public Car(Long id, ParkingSpot occupiedSpot) {
    this.id = id;
    this.occupiedSpot = occupiedSpot;
  }

  public Car() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ParkingSpot getOccupiedSpot() {
    return occupiedSpot;
  }

  public void setOccupiedSpot(ParkingSpot occupiedSpot) {
    this.occupiedSpot = occupiedSpot;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, occupiedSpot);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Car)) {
      return false;
    }
    Car other = (Car)obj;
    return Objects.equals(id, other.id) && Objects.equals(occupiedSpot, other.occupiedSpot);
  }

  @Override
  public String toString() {
    return "Car [id=" + id + ", occupiedSpot=" + occupiedSpot + "]";
  }
}
