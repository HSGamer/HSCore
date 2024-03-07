package me.hsgamer.hscore.expansion.extra.manager;

import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;
import me.hsgamer.hscore.expansion.common.exception.InvalidExpansionDescriptionException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Sort and filter the expansions based on their dependencies
 */
public interface DependableExpansionSortAndFilter extends UnaryOperator<Map<String, ExpansionClassLoader>> {
  /**
   * Get the dependencies of the expansion
   *
   * @param loader the loader
   *
   * @return the dependencies
   */
  List<String> getDependencies(ExpansionClassLoader loader);

  /**
   * Get the soft dependencies of the expansion.
   * This will not throw an exception if the soft dependency is not loaded.
   *
   * @param loader the loader
   *
   * @return the soft dependencies
   */
  List<String> getSoftDependencies(ExpansionClassLoader loader);

  /**
   * Get the comparator for the dependencies
   *
   * @return the comparator
   */
  default Comparator<Map.Entry<String, ExpansionClassLoader>> getDependencyComparator() {
    return (entry1, entry2) -> {
      ExpansionClassLoader loader1 = entry1.getValue();
      String name1 = entry1.getKey();
      List<String> depends1 = getDependencies(loader1);
      List<String> softDepends1 = getSoftDependencies(loader1);

      ExpansionClassLoader loader2 = entry2.getValue();
      String name2 = entry2.getKey();
      List<String> depends2 = getDependencies(loader2);
      List<String> softDepends2 = getSoftDependencies(loader2);

      depends1 = depends1 == null ? Collections.emptyList() : depends1;
      softDepends1 = softDepends1 == null ? Collections.emptyList() : softDepends1;

      depends2 = depends2 == null ? Collections.emptyList() : depends2;
      softDepends2 = softDepends2 == null ? Collections.emptyList() : softDepends2;

      if (depends1.contains(name2) || softDepends1.contains(name2)) {
        return 1;
      } else if (depends2.contains(name1) || softDepends2.contains(name1)) {
        return -1;
      } else {
        return 0;
      }
    };
  }

  @Override
  default Map<String, ExpansionClassLoader> apply(Map<String, ExpansionClassLoader> original) {
    Map<String, ExpansionClassLoader> sorted = new LinkedHashMap<>();
    Map<String, ExpansionClassLoader> remaining = new HashMap<>();

    // Start with loaders with no dependency and get the remaining
    Consumer<Map.Entry<String, ExpansionClassLoader>> consumer = entry -> {
      ExpansionClassLoader loader = entry.getValue();
      if (getDependencies(loader).isEmpty() && getSoftDependencies(loader).isEmpty()) {
        sorted.put(entry.getKey(), entry.getValue());
      } else {
        remaining.put(entry.getKey(), entry.getValue());
      }
    };
    original.entrySet().forEach(consumer);

    // Organize the remaining
    if (remaining.isEmpty()) {
      return sorted;
    }

    remaining.entrySet().stream().filter(entry -> {
      ExpansionClassLoader loader = entry.getValue();
      String name = entry.getKey();

      // Check if the required dependencies are loaded
      List<String> depends = getDependencies(loader);
      if (depends.isEmpty()) {
        return true;
      }

      for (String depend : depends) {
        if (!original.containsKey(depend)) {
          loader.setThrowable(new InvalidExpansionDescriptionException("Missing dependency for " + name + ": " + depend));
          return false;
        }
      }

      return true;
    }).sorted(getDependencyComparator()).forEach(entry -> sorted.put(entry.getKey(), entry.getValue()));

    return sorted;
  }
}
