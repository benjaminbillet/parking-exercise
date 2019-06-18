package exercise.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Parking lot entity.
 */
@Entity
@Table(name = "parking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parking implements Serializable {

  private static final long serialVersionUID = 1455653421473130448L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "name")
  private String name;

  @NotNull
  @ManyToOne
  private PricingType pricing;

  public Parking(Long id, String name, PricingType pricing) {
    this.id = id;
    this.name = name;
    this.pricing = pricing;
  }

  public Parking() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PricingType getPricing() {
    return pricing;
  }

  public void setPricing(PricingType pricing) {
    this.pricing = pricing;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, pricing);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Parking)) {
      return false;
    }
    Parking other = (Parking)obj;
    return Objects.equals(id, other.id) && Objects.equals(name, other.name)
        && Objects.equals(pricing, other.pricing);
  }

  @Override
  public String toString() {
    return "Parking [id=" + id + ", name=" + name + ", pricing=" + pricing + "]";
  }
}
