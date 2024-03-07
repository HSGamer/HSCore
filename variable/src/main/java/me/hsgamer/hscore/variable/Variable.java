package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.StringReplacer;

/**
 * A variable
 */
public class Variable {
  /**
   * The prefix of the variable
   */
  public final String prefix;
  /**
   * Whether the manager should check if the whole string matches the prefix, or just the start
   */
  public final boolean isWhole;
  /**
   * The string replacer
   */
  public final StringReplacer replacer;

  Variable(String prefix, boolean isWhole, StringReplacer replacer) {
    this.prefix = prefix;
    this.isWhole = isWhole;
    this.replacer = replacer;
  }
}
