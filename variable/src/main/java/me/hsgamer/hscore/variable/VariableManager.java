package me.hsgamer.hscore.variable;

import io.github.projectunified.maptemplate.MapTemplate;
import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The variable manager for the instance
 */
public class VariableManager implements StringReplacer {
  private final List<Variable> variableEntries = new ArrayList<>();
  private final List<StringReplacer> externalReplacers = new ArrayList<>();

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

  /**
   * Add an external replacer
   *
   * @param replacer the external string replacer
   */
  public void addExternalReplacer(StringReplacer replacer) {
    externalReplacers.add(replacer);
  }

  /**
   * Remove an external replacer
   *
   * @param replacer the external string replacer
   */
  public void removeExternalReplacer(StringReplacer replacer) {
    externalReplacers.remove(replacer);
  }

  /**
   * Clear all external replacers
   */
  public void clearExternalReplacers() {
    externalReplacers.clear();
  }

  /**
   * Get all external replacers
   *
   * @return the external replacers
   */
  public List<StringReplacer> getExternalReplacers() {
    return Collections.unmodifiableList(externalReplacers);
  }

  /**
   * Replace the variables of the string until it cannot be replaced anymore
   *
   * @param message the string
   * @param uuid    the unique id
   *
   * @return the replaced string
   */
  @NotNull
  public String setVariables(@NotNull String message, @Nullable UUID uuid) {
    String old;
    do {
      old = message;
      message = setSingleVariables(message, uuid);
    } while (!old.equals(message));
    return message;
  }

  /**
   * Replace the variables of the string (single time)
   *
   * @param message the string
   * @param uuid    the unique id
   *
   * @return the replaced string
   */
  @NotNull
  public String setSingleVariables(@NotNull String message, @Nullable UUID uuid) {
    message = Objects.toString(
      MapTemplate.builder()
        .setVariableFunction(identifier -> variableEntries.stream()
          .filter(entry -> entry.isWhole ? identifier.equalsIgnoreCase(entry.prefix) : identifier.toLowerCase(Locale.ROOT).startsWith(entry.prefix.toLowerCase(Locale.ROOT)))
          .findFirst()
          .map(entry -> entry.replacer.tryReplace(identifier.substring(entry.prefix.length()), uuid))
          .orElse(null))
        .build()
        .apply(message)
    );

    for (StringReplacer externalStringReplacer : externalReplacers) {
      String replaced = externalStringReplacer.tryReplace(message, uuid);
      if (replaced != null) {
        message = replaced;
      }
    }

    return message;
  }

  @Override
  public @Nullable String replace(@NotNull String original) {
    return setVariables(original, null);
  }

  @Override
  public @Nullable String replace(@NotNull String original, @NotNull UUID uuid) {
    return setVariables(original, uuid);
  }
}
