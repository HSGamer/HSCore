package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string ends with the 2nd string <br> Ex: STREDW("String", "ing")
 */
public class EndsWith extends StringComparator {

  public EndsWith() {
    super("STREDW");
  }

  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.endsWith(s2);
  }
}
