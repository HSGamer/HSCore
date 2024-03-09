package me.hsgamer.hscore.action.builder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The input for the {@link ActionBuilder}
 */
public class ActionInput {
  public final String type;
  public final String value;
  public final String option;

  /**
   * Create a new input
   *
   * @param type   the type of the action
   * @param value  the value of the action
   * @param option the option of the action
   */
  public ActionInput(String type, String value, String option) {
    this.type = type;
    this.value = value;
    this.option = option;
  }

  /**
   * Get the option as a stream
   *
   * @param separator the separator
   *
   * @return the list
   */
  public Stream<String> getOptionStream(String separator) {
    return option.isEmpty() ? Stream.empty() : Arrays.stream(option.split(separator)).map(String::trim);
  }

  /**
   * Get the option as a stream.
   * The format is {@code value,value}
   *
   * @return the list
   *
   * @see #getOptionStream(String)
   */
  public Stream<String> getOptionStream() {
    return getOptionStream(",");
  }

  /**
   * Get the option as a list
   *
   * @param separator the separator
   *
   * @return the list
   *
   * @see #getOptionStream(String)
   */
  public List<String> getOptionAsList(String separator) {
    return getOptionStream(separator).collect(Collectors.toList());
  }

  /**
   * Get the option as a list.
   * The format is {@code value,value}
   *
   * @return the list
   *
   * @see #getOptionStream()
   */
  public List<String> getOptionAsList() {
    return getOptionStream().collect(Collectors.toList());
  }

  /**
   * Get the option as a map.
   * The format is {@code key=value,key=value}
   *
   * @return the map
   */
  public Map<String, String> getOptionAsMap() {
    return getOptionStream()
      .map(s -> s.split("="))
      .collect(Collectors.toMap(strings -> strings[0].trim(), strings -> strings.length > 1 ? strings[1].trim() : ""));
  }
}
