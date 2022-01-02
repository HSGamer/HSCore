package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string is the same as the 2nd string (case-insensitive) <br> Ex: STREQIC("Str1",
 * "str1")
 */
public class EqualsIgnoreCase extends StringComparator {

  public EqualsIgnoreCase() {
    super("STREQIC");
  }

  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.equalsIgnoreCase(s2);
  }
}
