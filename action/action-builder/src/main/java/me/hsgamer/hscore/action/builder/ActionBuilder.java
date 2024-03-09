package me.hsgamer.hscore.action.builder;

import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.builder.MassBuilder;
import me.hsgamer.hscore.common.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The builder for the {@link Action}
 */
public final class ActionBuilder extends MassBuilder<ActionInput, Action> {
  /**
   * The instance of the action builder
   */
  public static final ActionBuilder INSTANCE = new ActionBuilder();
  /**
   * The pattern for the action.
   * The format is: {@code <type>(<option>): <value>}. Note that the {@code <option>} and {@code <value>} are optional.
   * Also, the allowed characters of the {@code <type>} are alphanumeric, {@code _}, {@code -} and {@code $}.
   * To get the {@code <type>}, {@code <option>} and {@code <value>}, use {@link Matcher#group(int)} with the index 1, 3 and 5 respectively.
   */
  public static final Pattern ACTION_PATTERN = Pattern.compile("\\s*([\\w\\-$]+)\\s*(\\((.*)\\))?\\s*(:\\s*(.*)\\s*)?");

  private ActionBuilder() {
    // EMPTY
  }

  /**
   * Register a new action creator
   *
   * @param creator the creator
   * @param type    the type
   */
  public void register(Function<ActionInput, Action> creator, String... type) {
    register(input -> {
      String action = input.type;
      for (String s : type) {
        if (action.equalsIgnoreCase(s)) {
          return Optional.of(creator.apply(input));
        }
      }
      return Optional.empty();
    });
  }

  /**
   * Build a list of actions
   *
   * @param list                  the list of strings
   * @param defaultActionFunction the default action function
   *
   * @return the list of actions
   */
  public List<Action> build(List<String> list, Function<ActionInput, Action> defaultActionFunction) {
    return list
      .stream()
      .flatMap(string -> {
        ActionInput input;
        Matcher matcher = ACTION_PATTERN.matcher(string);
        if (matcher.matches()) {
          String type = matcher.group(1);
          String option = Optional.ofNullable(matcher.group(3)).orElse("");
          String value = Optional.ofNullable(matcher.group(5)).orElse("");
          input = new ActionInput(type, value, option);
        } else {
          input = new ActionInput("", string, "");
        }
        return Stream.of(build(input).orElseGet(() -> defaultActionFunction.apply(input)));
      })
      .collect(Collectors.toList());
  }

  /**
   * Build a list of actions
   *
   * @param object                the object
   * @param defaultActionFunction the default action function
   *
   * @return the list of actions
   */
  public List<Action> build(Object object, Function<ActionInput, Action> defaultActionFunction) {
    return build(CollectionUtils.createStringListFromObject(object, true), defaultActionFunction);
  }
}
