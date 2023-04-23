package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The Global Variable Manager
 */
public final class GlobalVariableManager {
  private static final VariableManager VARIABLE_MANAGER = new VariableManager();
  private static final List<StringReplacer> EXTERNAL_REPLACERS = new ArrayList<>();

  private GlobalVariableManager() {
    // EMPTY
  }

  /**
   * Get the global variable manager
   *
   * @return the global variable manager
   */
  public static VariableManager getVariableManager() {
    return VARIABLE_MANAGER;
  }

  /**
   * Add an external replacer
   *
   * @param replacer the external string replacer
   */
  public static void addExternalReplacer(StringReplacer replacer) {
    EXTERNAL_REPLACERS.add(replacer);
  }

  /**
   * Clear all external replacers
   */
  public static void clearExternalReplacers() {
    EXTERNAL_REPLACERS.clear();
  }

  /**
   * Get all external replacers
   *
   * @return the external replacers
   */
  public static List<StringReplacer> getExternalReplacers() {
    return Collections.unmodifiableList(EXTERNAL_REPLACERS);
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the Variable object
   */
  public static void register(String prefix, StringReplacer variable) {
    VARIABLE_MANAGER.register(prefix, variable);
  }

  /**
   * Unregister a variable
   *
   * @param prefix the prefix
   */
  public static void unregister(String prefix) {
    VARIABLE_MANAGER.unregister(prefix);
  }

  /**
   * Get all variables
   *
   * @return the variables
   */
  public static Map<String, StringReplacer> getVariables() {
    return VARIABLE_MANAGER.getVariables();
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
  public static String setVariables(@NotNull String message, @Nullable UUID uuid) {
    String old;
    do {
      old = message;
      message = VARIABLE_MANAGER.setSingleVariables(message, uuid);
      for (StringReplacer externalStringReplacer : EXTERNAL_REPLACERS) {
        String replaced = externalStringReplacer.tryReplace(message, uuid);
        if (replaced != null) {
          message = replaced;
        }
      }
    } while (!old.equals(message));
    return message;
  }
}
