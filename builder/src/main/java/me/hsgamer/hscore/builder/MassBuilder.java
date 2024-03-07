package me.hsgamer.hscore.builder;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The builder that can build multiple outputs from the input.
 * The build element is a {@link Function} that takes the input and returns the {@link Optional} output.
 * The {@link Optional} output can be empty if the build element cannot build from the input.
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 */
public class MassBuilder<I, O> {
  private final ArrayDeque<Function<I, Optional<O>>> elements = new ArrayDeque<>();

  /**
   * Register a new build element
   *
   * @param element  the element
   * @param addFirst true if you want to add the element to the first of the list
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> register(Function<I, Optional<O>> element, boolean addFirst) {
    if (addFirst) {
      elements.addFirst(element);
    } else {
      elements.addLast(element);
    }
    return this;
  }

  /**
   * Register a new build element
   *
   * @param element the element
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> register(Function<I, Optional<O>> element) {
    return register(element, false);
  }

  /**
   * Remove a build element
   *
   * @param element the element
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> remove(Function<I, Optional<O>> element) {
    elements.remove(element);
    return this;
  }

  /**
   * Clear all the registered build elements
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> clear() {
    elements.clear();
    return this;
  }

  /**
   * Build the collection of outputs from the input
   *
   * @param input the input
   *
   * @return the collection of outputs
   */
  public Collection<O> buildAll(I input) {
    return elements.parallelStream()
      .map(element -> element.apply(input))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
  }

  /**
   * Build the output from the input.
   * This will find the first build element that can build the input, and return the output.
   * If there is no output, return an empty optional.
   *
   * @param input the input
   *
   * @return the output
   */
  public Optional<O> build(I input) {
    return elements.stream()
      .map(element -> element.apply(input))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .findFirst();
  }

  /**
   * Get the registered build elements
   *
   * @return the registered build elements
   */
  public Collection<Function<I, Optional<O>>> getElements() {
    return Collections.unmodifiableCollection(elements);
  }
}
