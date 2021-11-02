package me.hsgamer.hscore.crontime;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple cron-time manager to manage next execution time
 */
public class CronTimeManager {
  /**
   * The internal logger
   */
  protected static final Logger LOGGER = Logger.getLogger(CronTimeManager.class.getSimpleName());
  private final List<Cron> cronList;

  /**
   * Create a new manager
   *
   * @param cronList the cron list
   */
  public CronTimeManager(List<Cron> cronList) {
    this.cronList = cronList;
  }

  /**
   * Create a new manager
   *
   * @param crons the cron list
   */
  public CronTimeManager(Cron... crons) {
    this.cronList = Arrays.asList(crons);
  }

  /**
   * Create a new manager
   *
   * @param definition  the cron definition
   * @param cronStrings the cron string list
   */
  public CronTimeManager(CronDefinition definition, List<String> cronStrings) {
    this.cronList = new ArrayList<>();
    CronParser parser = new CronParser(definition);
    cronStrings.forEach(cronTime -> {
      try {
        Cron cron = parser.parse(cronTime);
        cronList.add(cron);
      } catch (Exception ex) {
        LOGGER.log(Level.WARNING, ex, () -> String.format("Cron time is invalid: `%s`", cronTime));
      }
    });
  }

  /**
   * Create a new manager
   *
   * @param definition  the cron definition
   * @param cronStrings the cron string list
   */
  public CronTimeManager(CronDefinition definition, String... cronStrings) {
    this(definition, Arrays.asList(cronStrings));
  }

  /**
   * Create a new manager
   *
   * @param cronType    the cron type
   * @param cronStrings the cron string list
   */
  public CronTimeManager(CronType cronType, List<String> cronStrings) {
    this(CronDefinitionBuilder.instanceDefinitionFor(cronType), cronStrings);
  }

  /**
   * Create a new manager
   *
   * @param cronType    the cron type
   * @param cronStrings the cron string list
   */
  public CronTimeManager(CronType cronType, String... cronStrings) {
    this(cronType, Arrays.asList(cronStrings));
  }

  /**
   * Get the next time from now
   *
   * @return the next time
   */
  public ZonedDateTime getNextTime() {
    return getNextTime(ZonedDateTime.now());
  }

  /**
   * Get the next time from the current time
   *
   * @param currentTime the current time
   *
   * @return the next time
   */
  public ZonedDateTime getNextTime(ZonedDateTime currentTime) {
    long currentMillis = currentTime.toInstant().toEpochMilli();

    long minDelayMillis = -1;
    ZonedDateTime nextDateTime = currentTime;
    for (Cron cron : cronList) {
      Optional<ZonedDateTime> optionalTime = ExecutionTime.forCron(cron).nextExecution(currentTime);
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
   * Get the next epoch millis from the current time
   *
   * @param currentTime the current time
   *
   * @return the epoch millis
   */
  public long getNextEpochMillis(ZonedDateTime currentTime) {
    return getNextTime(currentTime).toInstant().toEpochMilli();
  }

  /**
   * Get the cron list
   *
   * @return the cron list
   */
  public List<Cron> getCronList() {
    return Collections.unmodifiableList(cronList);
  }
}
