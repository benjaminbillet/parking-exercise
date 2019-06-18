package exercise.dto;

import java.time.Instant;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class BillingDto {

  public static final String ENTITY_NAME = "billing";

  private Double charged;

  private Instant time;
  
  public BillingDto() {
  }
  
  public BillingDto(@NotNull @PositiveOrZero Double charged) {
    this(charged, Instant.now());
  }

  public BillingDto(@NotNull @PositiveOrZero Double charged, @NotNull Instant time) {
    this.charged = charged;
    this.time = time;
  }

  public Double getCharged() {
    return charged;
  }

  public void setCharged(Double charged) {
    this.charged = charged;
  }

  public Instant getTime() {
    return time;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  @Override
  public int hashCode() {
    return Objects.hash(charged, time);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BillingDto)) {
      return false;
    }
    BillingDto other = (BillingDto)obj;
    return Objects.equals(charged, other.charged) && Objects.equals(time, other.time);
  }

  @Override
  public String toString() {
    return "BillingDto [charged=" + charged + ", time=" + time + "]";
  }
}
