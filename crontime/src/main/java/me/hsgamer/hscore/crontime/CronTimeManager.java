package me.hsgamer.hscore.crontime;

import com.cronutils.model.Cron;
import com.cronutils.model.time.ExecutionTime;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A simple cron-time manager to manage next execution time
 */
public class CronTimeManager {
  private final List<Cron> cronList;

  /**
   * Create a new manager
   *
   * @param cronList the cron list
   */
  public CronTimeManager(@NotNull List<Cron> cronList) {
    this.cronList = cronList;
  }

  /**
   * Create a new manager
   *
   * @param crons the cron list
   */
  public CronTimeManager(@NotNull Cron... crons) {
    this.cronList = Arrays.asList(crons);
  }

  /**
   * Get the next time from the initial time
   *
   * @param initTime the initial time to get the next time
   *
   * @return the next time
   */
  @NotNull
  public ZonedDateTime getNextTime(@NotNull ZonedDateTime initTime) {
    long currentMillis = initTime.toInstant().toEpochMilli();

    long minDelayMillis = -1;
    ZonedDateTime nextDateTime = initTime;
    for (Cron cron : cronList) {
      Optional<ZonedDateTime> optionalTime = ExecutionTime.forCron(cron).nextExecution(initTime);
      if (optionalTime.isPresent()) {
        ZonedDateTime time = optionalTime.get();
        long delayMillis = time.toInstant().toEpochMilli() - currentMillis;
        if (delayMillis < minDelayMillis || minDelayMillis < 0) {
          minDelayMillis = delayMillis;
          nextDateTime = time;
        }
      }
    }

    return nextDateTime;
  }

  /**
   * Get the next time from now
   *
   * @return the next time
   */
  @NotNull
  public ZonedDateTime getNextTime() {
    return getNextTime(ZonedDateTime.now());
  }

  /**
   * Get the next instant from the initial instant
   *
   * @param initInstant the initial instant to get the next instant
   *
   * @return the next instant
   */
  @NotNull
  public Instant getNextInstant(@NotNull Instant initInstant) {
    ZonedDateTime initTime = ZonedDateTime.ofInstant(initInstant, TimeZone.getDefault().toZoneId());
    ZonedDateTime nextTime = getNextTime(initTime);
    return nextTime.toInstant();
  }

  /**
   * Get the next instant from now
   *
   * @return the next instant
   */
  @NotNull
  public Instant getNextInstant() {
    return getNextInstant(Instant.now());
  }

  /**
   * Get the next epoch millis from the initial time
   *
   * @param initTime the initial time to get the next time
   *
   * @return the epoch millis
   */
  public long getNextEpochMillis(@NotNull ZonedDateTime initTime) {
    return getNextTime(initTime).toInstant().toEpochMilli();
  }

  /**
   * Get the next epoch millis from now
   *
   * @return the epoch millis
   */
  public long getNextEpochMillis() {
    return getNextEpochMillis(ZonedDateTime.now());
  }

  /**
   * Get the next epoch millis from now
   *
   * @param initInstant the initial instant to get the next time
   *
   * @return the epoch millis
   */
  public long getNextEpochMillis(@NotNull Instant initInstant) {
    return getNextEpochMillis(ZonedDateTime.ofInstant(initInstant, TimeZone.getDefault().toZoneId()));
  }

  /**
   * Get the remaining millis from now to the next time
   *
   * @param initTime the initial time to get the next time
   *
   * @return the millis
   */
  public long getRemainingMillis(@NotNull ZonedDateTime initTime) {
    return getNextEpochMillis(initTime) - initTime.toInstant().toEpochMilli();
  }

  /**
   * Get the remaining millis from now to the next time
   *
   * @return the millis
   */
  public long getRemainingMillis() {
    return getRemainingMillis(ZonedDateTime.now());
  }

  /**
   * Get the remaining millis from now to the next time
   *
   * @param initInstant the initial instant to get the next time
   *
   * @return the millis
   */
  public long getRemainingMillis(@NotNull Instant initInstant) {
    return getRemainingMillis(ZonedDateTime.ofInstant(initInstant, TimeZone.getDefault().toZoneId()));
  }

  /**
   * Get the cron list
   *
   * @return the cron list
   */
  @NotNull
  public List<Cron> getCronList() {
    return Collections.unmodifiableList(cronList);
  }
}
