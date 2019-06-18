package exercise.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "parking_spot_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingSpotType implements Serializable {

  private static final long serialVersionUID = -390354201403776836L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull
  @Column(name = "label")
  private String label;

  public ParkingSpotType() {
  }

  public ParkingSpotType(Long id, @NotNull String label) {
    this.id = id;
    this.label = label;
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

  @Override
  public int hashCode() {
    return Objects.hash(id, label);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ParkingSpotType)) {
      return false;
    }
    ParkingSpotType other = (ParkingSpotType)obj;
    return Objects.equals(id, other.id) && Objects.equals(label, other.label);
  }

  @Override
  public String toString() {
    return "ParkingSpotType [id=" + id + ", label=" + label + "]";
  }
}
