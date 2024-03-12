package me.hsgamer.hscore.builder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The mass builder for the functional value.
 * This is useful for builders where the input contains a type and the type is used to determine the creator.
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 */
public abstract class FunctionalMassBuilder<I, O> extends MassBuilder<I, O> {
  protected abstract String getType(I input);

  /**
   * Register a new creator
   *
   * @param creator the creator
   * @param type    the type
   */
  public void register(Function<I, O> creator, String... type) {
    register(input -> {
      String action = getType(input);
      for (String s : type) {
        if (action.equalsIgnoreCase(s)) {
          return Optional.of(creator.apply(input));
        }
      }
      return Optional.empty();
    });
  }

  /**
   * Build the value from the input
   *
   * @param list                 the input
   * @param defaultValueFunction the default value function
   *
   * @return the value
   */
  public List<O> build(List<I> list, Function<I, O> defaultValueFunction) {
    return list
      .stream()
      .flatMap(input -> build(input).map(Stream::of).orElseGet(() -> Stream.of(defaultValueFunction.apply(input))))
      .collect(Collectors.toList());
  }
}
