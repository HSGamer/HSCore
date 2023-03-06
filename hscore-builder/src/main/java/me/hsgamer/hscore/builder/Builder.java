package me.hsgamer.hscore.builder;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The simple builder
 *
 * @param <R> the type of the raw value
 * @param <V> the type of the final value
 */
public class Builder<R, V> extends MassBuilder<AbstractMap.SimpleEntry<String, R>, V> {
  /**
   * Register a new function
   *
   * @param biFunction the function
   * @param name       the name of the modifier
   *
   * @return the registered function element
   */
  public FunctionElement<R, V> register(BiFunction<String, R, V> biFunction, String... name) {
    FunctionElement<R, V> element = new FunctionElement<>(biFunction, name);
    register(element);
    return element;
  }

  /**
   * Register a new function
   *
   * @param function the function
   * @param name     the name of the modifier
   *
   * @return the registered function element
   */
  public FunctionElement<R, V> register(Function<R, V> function, String... name) {
    return register((s, r) -> function.apply(r), name);
  }

  /**
   * Register a new supplier
   *
   * @param supplier the supplier
   * @param name     the name of the modifier
   *
   * @return the registered function element
   */
  public FunctionElement<R, V> register(Supplier<V> supplier, String... name) {
    return register((s, r) -> supplier.get(), name);
  }

  /**
   * Build the final value from a raw value
   *
   * @param name     the name or the alias of the function
   * @param rawValue the raw value
   *
   * @return the final value
   */
  public Optional<V> build(String name, R rawValue) {
    return build(new AbstractMap.SimpleEntry<>(name, rawValue));
  }

  /**
   * Build the map of final values
   *
   * @param rawMap the map of raw values
   *
   * @return the map of final values
   */
  public Map<String, V> build(Map<String, R> rawMap) {
    Map<String, V> map = new LinkedHashMap<>();
    rawMap.forEach((name, raw) -> build(name, raw).ifPresent(v -> map.put(name.toLowerCase(Locale.ROOT), v)));
    return map;
  }

  /**
   * Get the registered map
   *
   * @return the registered map
   */
  public Map<String, BiFunction<String, R, V>> getRegisteredMap() {
    Map<String, BiFunction<String, R, V>> map = new HashMap<>();
    getElements().forEach(element -> {
      if (element instanceof FunctionElement) {
        FunctionElement<R, V> functionElement = (FunctionElement<R, V>) element;
        for (String name : functionElement.getNames()) {
          map.put(name.toLowerCase(Locale.ROOT), functionElement.getFunction());
        }
      }
    });
    return map;
  }

  /**
   * The function element
   *
   * @param <R> the type of the raw value
   * @param <V> the type of the final value
   */
  public static class FunctionElement<R, V> implements Element<AbstractMap.SimpleEntry<String, R>, V> {
    private final BiFunction<String, R, V> function;
    private final String[] names;

    /**
     * Create a new function element
     *
     * @param function the function
     * @param names    the names or the aliases of the function
     */
    public FunctionElement(BiFunction<String, R, V> function, String... names) {
      this.function = function;
      this.names = names;
    }

    @Override
    public boolean canBuild(AbstractMap.SimpleEntry<String, R> input) {
      String key = input.getKey();
      for (String alias : names) {
        if (key.equalsIgnoreCase(alias)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public V build(AbstractMap.SimpleEntry<String, R> input) {
      return function.apply(input.getKey(), input.getValue());
    }

    /**
     * Get the function
     *
     * @return the function
     */
    public BiFunction<String, R, V> getFunction() {
      return function;
    }

    /**
     * Get the names or the aliases of the function
     *
     * @return the names or the aliases of the function
     */
    public String[] getNames() {
      return names;
    }
  }
}
