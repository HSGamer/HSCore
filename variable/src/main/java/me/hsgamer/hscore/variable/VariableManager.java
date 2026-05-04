package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

/**
 * The variable manager for the instance
 */
public class VariableManager implements StringReplacer {
  private final List<Variable> variableEntries = new ArrayList<>();

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the replacer
   * @param isWhole  whether the manager should check the whole string matches the prefix, set it to false if you want to check if the prefix is at the beginning of the string
   */
  public void register(String prefix, StringReplacer variable, boolean isWhole) {
    variableEntries.add(new Variable(prefix, isWhole, variable));
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the Variable object
   */
  public void register(String prefix, StringReplacer variable) {
    register(prefix, variable, false);
  }

  /**
   * Unregister a variable
   *
   * @param prefix the prefix
   */
  public void unregister(String prefix) {
    variableEntries.removeIf(entry -> entry.prefix.equalsIgnoreCase(prefix));
  }

  /**
   * Get all variables
   *
   * @return the variables
   */
  public Map<String, StringReplacer> getVariables() {
    return variableEntries.stream().collect(HashMap::new, (map, entry) -> map.put(entry.prefix, entry.replacer), HashMap::putAll);
  }

  /**
   * Get all variable entries
   *
   * @return the variable entries
   */
  public List<Variable> getVariableEntries() {
    return Collections.unmodifiableList(variableEntries);
  }

  private @Nullable String replace(@NotNull String original, BiFunction<StringReplacer, String, String> function) {
    return variableEntries.stream()
      .filter(entry -> entry.isWhole ? original.equalsIgnoreCase(entry.prefix) : original.toLowerCase(Locale.ROOT).startsWith(entry.prefix.toLowerCase(Locale.ROOT)))
      .findFirst()
      .map(entry -> function.apply(entry.replacer, original.substring(entry.prefix.length())))
      .orElse(null);
  }

  @Override
  public @Nullable String replace(@NotNull String original) {
    return replace(original, StringReplacer::replace);
  }

  @Override
  public @Nullable String replace(@NotNull String original, @NotNull UUID uuid) {
    return replace(original, (replacer, remain) -> replacer.replace(remain, uuid));
  }
}
