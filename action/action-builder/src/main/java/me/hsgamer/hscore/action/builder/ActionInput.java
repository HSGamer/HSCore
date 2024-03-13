package me.hsgamer.hscore.action.builder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The input for the {@link ActionBuilder}
 */
public interface ActionInput {
  /**
   * The pattern to parse the input from the string.
   * The format is: {@code <type>(<option>): <value>}. Note that the {@code <option>} and {@code <value>} are optional.
   * Also, the allowed characters of the {@code <type>} are alphanumeric, {@code _}, {@code -} and {@code $}.
   * To get the {@code <type>}, {@code <option>} and {@code <value>}, use {@link Matcher#group(int)} with the index 1, 3 and 5 respectively.
   */
  Pattern PATTERN = Pattern.compile("\\s*([\\w\\-$]+)\\s*(\\((.*)\\))?\\s*(:\\s*(.*)\\s*)?");

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
   * Create an instance of {@link ActionInput} from the input.
   * It will use the {@link #PATTERN} to parse the input.
   * If the input doesn't match the pattern, it will use the input as the value.
   *
   * @param input the input
   *
   * @return the instance
   *
   * @see #PATTERN
   */
  static ActionInput create(String input) {
    Matcher matcher = PATTERN.matcher(input);
    if (matcher.matches()) {
      String type = matcher.group(1);
      String option = Optional.ofNullable(matcher.group(3)).orElse("");
      String value = Optional.ofNullable(matcher.group(5)).orElse("");
      return create(type, option, value);
    } else {
      return create("", "", input);
    }
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
