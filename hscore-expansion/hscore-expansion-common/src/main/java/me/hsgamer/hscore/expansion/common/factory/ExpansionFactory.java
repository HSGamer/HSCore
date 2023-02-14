package me.hsgamer.hscore.expansion.common.factory;

import me.hsgamer.hscore.expansion.common.exception.InvalidExpansionDescription;
import me.hsgamer.hscore.expansion.common.object.Expansion;
import me.hsgamer.hscore.expansion.common.object.ExpansionClassLoader;

public interface ExpansionFactory<T extends Expansion> {
  /**
   * Create an expansion from the class loader
   */
  T create(ExpansionClassLoader<T> classLoader) throws InvalidExpansionDescription;
}
