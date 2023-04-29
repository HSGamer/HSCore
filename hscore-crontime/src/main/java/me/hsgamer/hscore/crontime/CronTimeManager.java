package me.hsgamer.hscore.crontime;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.provider.LoggerProvider;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * A simple cron-time manager to manage next execution time
 */
public class CronTimeManager {
  /**
   * The internal logger
   */
  protected static final Logger LOGGER = LoggerProvider.getLogger(CronTimeManager.class);
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
   * Create a new manager
   *
   * @param definition  the cron definition
   * @param cronStrings the cron string list
   */
  public CronTimeManager(@NotNull CronDefinition definition, @NotNull List<String> cronStrings) {
    this.cronList = new ArrayList<>();
    CronParser parser = new CronParser(definition);
    cronStrings.forEach(cronTime -> {
      try {
        Cron cron = parser.parse(cronTime);
        cronList.add(cron);
      } catch (Exception ex) {
        LOGGER.log(LogLevel.WARN, "Cron time is invalid: `" + cronTime + "`", ex);
      }
    });
  }

  /**
   * Create a new manager
   *
   * @param definition  the cron definition
   * @param cronStrings the cron string list
   */
  public CronTimeManager(@NotNull CronDefinition definition, @NotNull String... cronStrings) {
    this(definition, Arrays.asList(cronStrings));
  }

  /**
   * Create a new manager
   *
   * @param cronType    the cron type
   * @param cronStrings the cron string list
   */
  public CronTimeManager(@NotNull CronType cronType, @NotNull List<String> cronStrings) {
    this(CronDefinitionBuilder.instanceDefinitionFor(cronType), cronStrings);
  }

  /**
   * Create a new manager
   *
   * @param cronType    the cron type
   * @param cronStrings the cron string list
   */
  public CronTimeManager(@NotNull CronType cronType, @NotNull String... cronStrings) {
    this(cronType, Arrays.asList(cronStrings));
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
        if (delayMillis < minDelayMillis || minDelayMillis == -1) {
          minDelayMillis = delayMillis;
          nextDateTime = time;
        }
      }
    }

    return nextDateTime;
  }

  /**
   * Get the next epoch millis from now
   *
   * @return the epoch millis
   */
  public long getNextEpochMillis() {
    return getNextTime().toInstant().toEpochMilli();
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
   * @param initTime the initial time to get the next time
   *
   * @return the millis
   */
  public long getRemainingMillis(@NotNull ZonedDateTime initTime) {
    return getNextEpochMillis(initTime) - initTime.toInstant().toEpochMilli();
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
