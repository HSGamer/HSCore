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
   * @param aliases    the aliases of the modifier
   */
  public void register(BiFunction<String, R, V> biFunction, String name, String... aliases) {
    register(new Element<AbstractMap.SimpleEntry<String, R>, V>() {
      @Override
      public boolean canBuild(AbstractMap.SimpleEntry<String, R> input) {
        String key = input.getKey();
        if (key.equalsIgnoreCase(name)) {
          return true;
        }
        for (String alias : aliases) {
          if (key.equalsIgnoreCase(alias)) {
            return true;
          }
        }
        return false;
      }

      @Override
      public V build(AbstractMap.SimpleEntry<String, R> input) {
        return biFunction.apply(input.getKey(), input.getValue());
      }
    });
  }

  /**
   * Register a new function
   *
   * @param function the function
   * @param name     the name of the modifier
   * @param aliases  the aliases of the modifier
   */
  public void register(Function<R, V> function, String name, String... aliases) {
    register((s, r) -> function.apply(r), name, aliases);
  }

  /**
   * Register a new supplier
   *
   * @param supplier the supplier
   * @param name     the name of the modifier
   * @param aliases  the aliases of the modifier
   */
  public void register(Supplier<V> supplier, String name, String... aliases) {
    register((s, r) -> supplier.get(), name, aliases);
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
    Map<String, V> map = new HashMap<>();
    rawMap.forEach((name, raw) -> build(name, raw).ifPresent(v -> map.put(name.toLowerCase(Locale.ROOT), v)));
    return map;
  }
}
