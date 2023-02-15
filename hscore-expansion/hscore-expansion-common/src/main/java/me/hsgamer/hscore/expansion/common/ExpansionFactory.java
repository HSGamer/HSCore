package me.hsgamer.hscore.expansion.common;

public interface ExpansionFactory {
  /**
   * Create an expansion from the class loader
   */
  Expansion create(ExpansionClassLoader classLoader) throws InvalidExpansionException;
}
