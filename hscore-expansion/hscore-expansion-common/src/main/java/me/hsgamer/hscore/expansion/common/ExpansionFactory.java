package me.hsgamer.hscore.expansion.common;

public interface ExpansionFactory {
  ExpansionFactory DEFAULT = classLoader -> {
    try {
      final Class<?> clazz = Class.forName(classLoader.getAddonDescription().getMainClass(), true, classLoader);
      final Class<? extends Expansion> newClass;
      if (Expansion.class.isAssignableFrom(clazz)) {
        newClass = clazz.asSubclass(Expansion.class);
      } else {
        throw new ClassCastException("The main class does not extend " + Expansion.class.getSimpleName());
      }
      return newClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new InvalidExpansionException("Can't load the main class", e);
    }
  };

  /**
   * Create an expansion from the class loader
   */
  Expansion create(ExpansionClassLoader classLoader) throws InvalidExpansionException;
}
