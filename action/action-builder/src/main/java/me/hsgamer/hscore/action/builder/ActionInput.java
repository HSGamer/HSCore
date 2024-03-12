package me.hsgamer.hscore.action.builder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The input for the {@link ActionBuilder}
 */
public interface ActionInput {
  /**
   * Create an instance of {@link ActionInput}
   *
   * @param type   the type
   * @param option the option
   * @param value  the value
   *
   * @return the instance
   */
  static ActionInput create(String type, String option, String value) {
    return new ActionInput() {
      @Override
      public String getType() {
        return type;
      }

      @Override
      public String getOption() {
        return option;
      }

      @Override
      public String getValue() {
        return value;
      }
    };
  }

  /**
   * Get the type
   *
   * @return the type
   */
  String getType();

  /**
   * Get the option
   *
   * @return the option
   */
  String getOption();

  /**
   * Get the value
   *
   * @return the value
   */
  String getValue();

  /**
   * Get the option as a stream
   *
   * @param separator the separator
   *
   * @return the list
   */
  default Stream<String> getOptionStream(String separator) {
    String option = getOption();
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
  default Stream<String> getOptionStream() {
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
  default List<String> getOptionAsList(String separator) {
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
  default List<String> getOptionAsList() {
    return getOptionStream().collect(Collectors.toList());
  }

  /**
   * Get the option as a map.
   * The format is {@code key=value,key=value}
   *
   * @return the map
   */
  default Map<String, String> getOptionAsMap() {
    return getOptionStream()
      .map(s -> s.split("="))
      .collect(Collectors.toMap(strings -> strings[0].trim(), strings -> strings.length > 1 ? strings[1].trim() : ""));
  }
}
