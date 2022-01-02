package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string contains the 2nd string <br> Ex: STRCT("this", "is")
 */
public class Contains extends StringComparator {

  public Contains() {
    super("STRCT");
  }

  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.contains(s2);
  }
}
