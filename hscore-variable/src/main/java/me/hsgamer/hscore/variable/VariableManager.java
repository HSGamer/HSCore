package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.interfaces.StringReplacer;

import java.util.*;
import java.util.function.BooleanSupplier;

/**
 * The Variable Manager
 */
public final class VariableManager {
  private static final InstanceVariableManager globalVariableManager = new InstanceVariableManager();
  private static final List<ExternalStringReplacer> externalReplacers = new ArrayList<>();
  private static BooleanSupplier replaceAll = () -> false;

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
   * Get the status whether the manager should replace all similar variables on single time
   *
   * @return true if it should
   */
  public static boolean getReplaceAll() {
    return replaceAll.getAsBoolean();
  }

  /**
   * Whether the manager should replace all similar variables on single time
   *
   * @param replaceAll true if it should
   *
   * @see #setSingleVariables(String, UUID)
   */
  public static void setReplaceAll(boolean replaceAll) {
    setReplaceAll(() -> replaceAll);
  }

  /**
   * Whether the manager should replace all similar variables on single time
   *
   * @param replaceAll the boolean supplier (true if it should)
   *
   * @see #setSingleVariables(String, UUID)
   */
  public static void setReplaceAll(BooleanSupplier replaceAll) {
    VariableManager.replaceAll = replaceAll;
  }

  /**
   * Add an external replacer
   *
   * @param replacer the external string replacer
   */
  public static void addExternalReplacer(ExternalStringReplacer replacer) {
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
  public static List<ExternalStringReplacer> getExternalReplacers() {
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
   * Check if a string contains variables
   *
   * @param message the string
   *
   * @return true if it has, otherwise false
   */
  public static boolean hasVariables(String message) {
    return globalVariableManager.hasVariables(message) || externalReplacers.parallelStream().anyMatch(replacer -> replacer.canBeReplaced(message));
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
      message = setSingleVariables(message, uuid);
    } while (hasVariables(message) && !old.equals(message));
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
  public static String setSingleVariables(String message, UUID uuid) {
    message = globalVariableManager.setSingleVariables(message, uuid);
    for (ExternalStringReplacer externalStringReplacer : externalReplacers) {
      message = externalStringReplacer.replace(message, uuid);
    }
    return message;
  }
}
