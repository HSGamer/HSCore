package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string matches the RegEx pattern from the 2nd string <br> Ex: STRMP("String",
 * "String-?")
 */
public class MatchPattern extends StringComparator {

  public MatchPattern() {
    super("STRMP");
  }

  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.matches(s2);
  }
}
