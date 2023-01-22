package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.interfaces.StringReplacer;

import java.util.*;

/**
 * The Variable Manager
 */
public final class VariableManager {
  private static final InstanceVariableManager globalVariableManager = new InstanceVariableManager();
  private static final List<StringReplacer> externalReplacers = new ArrayList<>();

  private VariableManager() {
    // EMPTY
  }

  /**
   * Get the global variable manager
   *
   * @return the global variable manager
   */
  public static InstanceVariableManager getGlobalVariableManager() {
    return globalVariableManager;
  }

  /**
   * Add an external replacer
   *
   * @param replacer the external string replacer
   */
  public static void addExternalReplacer(StringReplacer replacer) {
    externalReplacers.add(replacer);
  }

  /**
   * Clear all external replacers
   */
  public static void clearExternalReplacers() {
    externalReplacers.clear();
  }

  /**
   * Get all external replacers
   *
   * @return the external replacers
   */
  public static List<StringReplacer> getExternalReplacers() {
    return Collections.unmodifiableList(externalReplacers);
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the Variable object
   */
  public static void register(String prefix, StringReplacer variable) {
    globalVariableManager.register(prefix, variable);
  }

  /**
   * Unregister a variable
   *
   * @param prefix the prefix
   */
  public static void unregister(String prefix) {
    globalVariableManager.unregister(prefix);
  }

  /**
   * Get all variables
   *
   * @return the variables
   */
  public static Map<String, StringReplacer> getVariables() {
    return globalVariableManager.getVariables();
  }

  /**
   * Replace the variables of the string until it cannot be replaced anymore
   *
   * @param message the string
   * @param uuid    the unique id
   *
   * @return the replaced string
   */
  public static String setVariables(String message, UUID uuid) {
    String old;
    do {
      old = message;
      message = globalVariableManager.setSingleVariables(message, uuid);
      for (StringReplacer externalStringReplacer : externalReplacers) {
        message = externalStringReplacer.replace(message, uuid);
      }
    } while (!old.equals(message));
    return message;
  }
}
