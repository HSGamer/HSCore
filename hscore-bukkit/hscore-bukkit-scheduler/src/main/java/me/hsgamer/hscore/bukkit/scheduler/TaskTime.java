package me.hsgamer.hscore.bukkit.scheduler;

import java.util.concurrent.TimeUnit;

/**
 * The time for the task
 */
public class TaskTime {
  private final long time;
  private final TimeUnit unit;
  private final long ticks;

  private TaskTime(long time, TimeUnit unit) {
    this.time = Math.max(time, 0);
    this.unit = unit;
    this.ticks = unit.toMillis(this.time) / 50;
  }

  /**
   * Create a new task time
   *
   * @param time the time
   * @param unit the unit
   *
   * @return the task time
   */
  public static TaskTime of(long time, TimeUnit unit) {
    return new TaskTime(time, unit);
  }

  /**
   * Create a new task time from ticks
   *
   * @param ticks the ticks
   *
   * @return the task time
   */
  public static TaskTime of(long ticks) {
    return new TaskTime(ticks * 50, TimeUnit.MILLISECONDS);
  }

  /**
   * Get the time
   *
   * @return the time
   */
  public long getTime() {
    return time;
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
   * Get the ticks
   *
   * @return the ticks
   */
  public long getTicks() {
    return ticks;
  }
}
