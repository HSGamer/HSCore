package me.hsgamer.hscore.bukkit.scheduler;

import java.util.concurrent.TimeUnit;

/**
 * The time for repeating task
 */
public class TimerTaskTime {
  private final long delay;
  private final long period;
  private final TimeUnit unit;
  private final long delayTicks;
  private final long periodTicks;
  private final boolean fromTicks;

  private TimerTaskTime(long delay, long period, TimeUnit unit, boolean fromTicks) {
    this.delay = Math.max(0, delay);
    this.period = Math.max(0, period);
    this.unit = unit;
    this.delayTicks = unit.toMillis(this.delay) / 50;
    this.periodTicks = unit.toMillis(this.period) / 50;
    this.fromTicks = fromTicks;
  }

  /**
   * Create a new time
   *
   * @param delay  the delay
   * @param period the period
   * @param unit   the unit
   *
   * @return the time
   */
  public static TimerTaskTime of(long delay, long period, TimeUnit unit) {
    return new TimerTaskTime(delay, period, unit, false);
  }

  /**
   * Create a new time from ticks
   *
   * @param delayTicks  the delay in ticks
   * @param periodTicks the period in ticks
   *
   * @return the time
   */
  public static TimerTaskTime of(long delayTicks, long periodTicks) {
    return new TimerTaskTime(delayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS, true);
  }

  /**
   * Get the delay
   *
   * @return the delay
   */
  public long getDelay() {
    return delay;
  }

  /**
   * Get the normalized delay.
   * If the delay is less than or equal to 0, it will return 1 or 50 (if fromTicks is true).
   *
   * @return the normalized delay
   */
  public long getNormalizedDelay() {
    if (delay <= 0) {
      return fromTicks ? 50 : 1;
    }
    return delay;
  }

  /**
   * Get the period
   *
   * @return the period
   */
  public long getPeriod() {
    return period;
  }

  /**
   * Get the normalized period.
   * If the period is less than or equal to 0, it will return 1 or 50 (if fromTicks is true).
   *
   * @return the normalized period
   */
  public long getNormalizedPeriod() {
    if (period <= 0) {
      return fromTicks ? 50 : 1;
    }
    return period;
  }

  /**
   * Get the unit
   *
   * @return the unit
   */
  public TimeUnit getUnit() {
    return unit;
  }

  /**
   * Get the delay in ticks
   *
   * @return the delay in ticks
   */
  public long getDelayTicks() {
    return delayTicks;
  }

  /**
   * Get the normalized delay in ticks.
   * If the delay is less than or equal to 0, it will return 1.
   *
   * @return the normalized delay in ticks
   */
  public long getNormalizedDelayTicks() {
    return Math.max(1, delayTicks);
  }

  /**
   * Get the period in ticks
   *
   * @return the period in ticks
   */
  public long getPeriodTicks() {
    return periodTicks;
  }

  /**
   * Get the normalized period in ticks.
   * If the period is less than or equal to 0, it will return 1.
   *
   * @return the normalized period in ticks
   */
  public long getNormalizedPeriodTicks() {
    return Math.max(1, periodTicks);
  }
}
