package exercise.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class PricingType implements Serializable {

  private static final long serialVersionUID = -7450282726497771463L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public PricingType(Long id) {
    this.id = id;
  }

  public PricingType() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PricingType)) {
      return false;
    }
    PricingType other = (PricingType)obj;
    return Objects.equals(id, other.id);
  }

  @Override
  public String toString() {
    return "PricingType [id=" + id + "]";
  }
}
