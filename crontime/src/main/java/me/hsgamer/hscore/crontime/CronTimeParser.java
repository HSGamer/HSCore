package me.hsgamer.hscore.crontime;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The cron time parser
 */
public class CronTimeParser {
  /**
   * Create a new cron list from the cron string list. The invalid cron strings will be ignored.
   *
   * @param definition  the cron definition
   * @param cronStrings the cron string list
   */
  public static List<Cron> parse(@NotNull CronDefinition definition, @NotNull List<String> cronStrings) {
    List<Cron> cronList = new ArrayList<>();
    CronParser parser = new CronParser(definition);
    cronStrings.forEach(cronTime -> {
      try {
        Cron cron = parser.parse(cronTime);
        cronList.add(cron);
      } catch (Exception exception) {
        // Ignore
      }
    });
    return cronList;
  }

  /**
   * Create a new cron list from the cron string array. The invalid cron strings will be ignored.
   *
   * @param definition  the cron definition
   * @param cronStrings the cron string array
   */
  public static List<Cron> parse(@NotNull CronDefinition definition, @NotNull String... cronStrings) {
    return parse(definition, Arrays.asList(cronStrings));
  }

  /**
   * Create a new cron list from the cron string list. The invalid cron strings will be ignored.
   *
   * @param type        the cron type
   * @param cronStrings the cron string list
   *
   * @return the cron list
   */
  public static List<Cron> parse(@NotNull CronType type, @NotNull List<String> cronStrings) {
    return parse(CronDefinitionBuilder.instanceDefinitionFor(type), cronStrings);
  }

  /**
   * Create a new cron list from the cron string array. The invalid cron strings will be ignored.
   *
   * @param type        the cron type
   * @param cronStrings the cron string array
   *
   * @return the cron list
   */
  public static List<Cron> parse(@NotNull CronType type, @NotNull String... cronStrings) {
    return parse(type, Arrays.asList(cronStrings));
  }
}
