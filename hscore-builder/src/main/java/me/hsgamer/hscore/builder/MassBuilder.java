package me.hsgamer.hscore.builder;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The builder
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 */
public class MassBuilder<I, O> {
  private final ArrayDeque<Element<I, O>> elements = new ArrayDeque<>();
  private boolean addFirst = false;

  /**
   * Set if the new element should be added at the first index of the registered elements.
   * If false, the new element will be added at the last index.
   * Enable this will make the new element to be the first element to be checked.
   *
   * @param addFirst true if the new element should be added first
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> setAddFirst(boolean addFirst) {
    this.addFirst = addFirst;
    return this;
  }

  /**
   * Register a new build element
   *
   * @param element the element
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> register(Element<I, O> element) {
    if (addFirst) {
      elements.addFirst(element);
    } else {
      elements.addLast(element);
    }
    return this;
  }

  /**
   * Remove a build element
   *
   * @param element the element
   *
   * @return this builder for chaining
   */
  public MassBuilder<I, O> remove(Element<I, O> element) {
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
   * Get the registered build elements
   *
   * @return the registered build elements
   */
  public Collection<Element<I, O>> getElements() {
    return Collections.unmodifiableCollection(elements);
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
