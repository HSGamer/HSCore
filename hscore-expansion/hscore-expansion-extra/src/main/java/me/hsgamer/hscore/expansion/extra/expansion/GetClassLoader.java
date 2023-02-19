package me.hsgamer.hscore.expansion.extra.expansion;

import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;

/**
 * An interface for {@link me.hsgamer.hscore.expansion.common.Expansion} to make it easier to get the class loader
 */
public interface GetClassLoader {
  /**
   * Get the class loader of the expansion
   *
   * @return the class loader
   */
  default ExpansionClassLoader getExpansionClassLoader() {
    ClassLoader classLoader = getClass().getClassLoader();
    if (!(classLoader instanceof ExpansionClassLoader)) {
      throw new IllegalStateException("Cannot create an addon without AddonClassLoader");
    }
    return (ExpansionClassLoader) classLoader;
  }
}
