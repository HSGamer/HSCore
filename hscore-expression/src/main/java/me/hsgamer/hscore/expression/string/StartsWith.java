package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string starts with the 2nd string <br> Ex: STRSTW("String", "Str")
 */
public class StartsWith extends StringComparator {

  public StartsWith() {
    super("STRSTW");
  }

  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.startsWith(s2);
  }
}
