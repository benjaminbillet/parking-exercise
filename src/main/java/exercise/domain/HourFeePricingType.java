package exercise.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Simple pricing data entity for a parking lot.
 */
@Entity
@Table(name = "hour_fee_pricing")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HourFeePricingType extends PricingType {

  private static final long serialVersionUID = -1515802732607634615L;

  @NotNull
  @Column(name = "minimum_fee")
  private Double minimumFee;

  @NotNull
  @Column(name = "hour_fee")
  private Double hourFee;

  public HourFeePricingType(Long id, Double minimumFee, Double hourFee) {
    super(id);
    this.minimumFee = minimumFee;
    this.hourFee = hourFee;
  }

  public HourFeePricingType() {
  }

  public Double getHourFee() {
    return hourFee;
  }

  public void setHourFee(Double hourFee) {
    this.hourFee = hourFee;
  }

  public Double getMinimumFee() {
    return minimumFee;
  }

  public void setMinimumFee(Double minimumFee) {
    this.minimumFee = minimumFee;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(hourFee, minimumFee);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof HourFeePricingType)) {
      return false;
    }
    HourFeePricingType other = (HourFeePricingType)obj;
    return Objects.equals(hourFee, other.hourFee) && Objects.equals(minimumFee, other.minimumFee);
  }

  @Override
  public String toString() {
    return "HourFeePricingType [minimumFee=" + minimumFee + ", hourFee=" + hourFee + "]";
  }
}
