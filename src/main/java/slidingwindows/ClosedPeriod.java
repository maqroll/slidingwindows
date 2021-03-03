package slidingwindows;

public class ClosedPeriod {
  private Long period;
  private Long value;

  public ClosedPeriod(Long period, Long value) {
    this.period = period;
    this.value = value;
  }

  public Long period() {
    return period;
  }

  public Long value() {
    return value;
  }

  @Override
  public String toString() {
    return "ClosedPeriod{" +
        "period=" + period +
        ", value=" + value +
        '}';
  }
}
