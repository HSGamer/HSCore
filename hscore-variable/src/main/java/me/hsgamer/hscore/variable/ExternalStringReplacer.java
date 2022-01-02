package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.interfaces.StringReplacer;

/**
 * An external string replacer used in {@link VariableManager}
 */
public interface ExternalStringReplacer extends StringReplacer {

  /**
   * Check if the string can be replaced
   *
   * @param string the string
   *
   * @return true if it can
   */
  default boolean canBeReplaced(String string) {
    return false;
  }
}
