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
   * Create an instance of {@link ActionInput} from the input.
   * It will parse the input to extract type, option, and value.
   * The format is: {@code <type>(<option>): <value>}. Note that the {@code <option>} and {@code <value>} are optional.
   * If the input doesn't match the expected format, it will use the input as the value.
   *
   * @param input the input
   *
   * @return the instance
   */
  static ActionInput create(String input) {
    input = input.trim();

    // Find the colon to separate type/option from value
    int colonIndex = input.indexOf(':');
    String typeOptionPart = colonIndex == -1 ? input : input.substring(0, colonIndex);
    String value = colonIndex == -1 ? "" : input.substring(colonIndex + 1).trim();

    typeOptionPart = typeOptionPart.trim();

    // Find the opening parenthesis to separate type from option
    int openParenIndex = typeOptionPart.indexOf('(');
    String type;
    String option = "";

    if (openParenIndex == -1) {
      type = typeOptionPart;
    } else {
      type = typeOptionPart.substring(0, openParenIndex).trim();
      int closeParenIndex = typeOptionPart.lastIndexOf(')');
      if (closeParenIndex > openParenIndex) {
        option = typeOptionPart.substring(openParenIndex + 1, closeParenIndex).trim();
      }
    }

    // If no type is found, use the entire input as value
    if (type.isEmpty() && value.isEmpty()) {
      return create("", "", input);
    }

    return create(type, option, value);
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
   * The map format is {@code key=value}
   *
   * @param separator the separator
   *
   * @return the map
   *
   * @see #getOptionStream(String)
   */
  default Map<String, String> getOptionAsMap(String separator) {
    return getOptionStream(separator)
      .map(s -> s.split("="))
      .collect(Collectors.toMap(strings -> strings[0].trim(), strings -> strings.length > 1 ? strings[1].trim() : ""));
  }

  /**
   * Get the option as a map.
   * The format is {@code key=value,key=value}
   *
   * @return the map
   *
   * @see #getOptionAsMap()
   */
  default Map<String, String> getOptionAsMap() {
    return getOptionAsMap(",");
  }
}
