package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.StringReplacer;

import java.util.HashSet;
import java.util.Set;

/**
 * A bundle of variables that is useful for grouping variables
 */
public class VariableBundle {
  private final Set<String> variablePrefix = new HashSet<>();
  private final VariableManager variableManager;

  /**
   * Create a new bundle for the variable manager
   *
   * @param variableManager the variable manager
   */
  public VariableBundle(VariableManager variableManager) {
    this.variableManager = variableManager;
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the replacer
   * @param isWhole  whether the manager should check the whole string matches the prefix, set it to false if you want to check if the prefix is at the beginning of the string
   *
   * @return true if the registration is successful or false if the prefix already exists
   */
  public boolean register(String prefix, StringReplacer variable, boolean isWhole) {
    if (variablePrefix.add(prefix)) {
      variableManager.register(prefix, variable, isWhole);
      return true;
    }
    return false;
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the Variable object
   *
   * @return true if the registration is successful or false if the prefix already exists
   */
  public boolean register(String prefix, StringReplacer variable) {
    return register(prefix, variable, false);
  }

  /**
   * Unregister a variable
   *
   * @param prefix the prefix
   *
   * @return true if the un-registration is successful or false if the prefix doesn't exist
   */
  public boolean unregister(String prefix) {
    if (variablePrefix.remove(prefix)) {
      variableManager.unregister(prefix);
      return true;
    }
    return false;
  }

  /**
   * Unregister all variables
   */
  public void unregisterAll() {
    variablePrefix.forEach(variableManager::unregister);
    variablePrefix.clear();
  }
}
