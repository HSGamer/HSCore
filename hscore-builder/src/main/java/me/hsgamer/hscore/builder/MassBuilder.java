package me.hsgamer.hscore.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The builder
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 */
public class MassBuilder<I, O> {
  private final List<Element<I, O>> elements = new LinkedList<>();

  /**
   * Register a new build element
   *
   * @param element the element
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> register(Element<I, O> element) {
    elements.add(element);
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
      .filter(element -> element.canBuild(input))
      .map(element -> element.build(input))
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
      .filter(element -> element.canBuild(input))
      .findFirst()
      .map(element -> element.build(input));
  }

  /**
   * The build element
   *
   * @param <I> the type of the input
   * @param <O> the type of the output
   */
  public interface Element<I, O> {
    /**
     * Check if the input can be built
     *
     * @param input the input
     *
     * @return true if the input can be built
     */
    boolean canBuild(I input);

    /**
     * Build the output from the input
     *
     * @param input the input
     *
     * @return the output
     */
    O build(I input);
  }
}
