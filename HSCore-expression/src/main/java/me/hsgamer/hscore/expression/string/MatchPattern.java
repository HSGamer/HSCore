package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;

public class MatchPattern extends StringComparator {

  public MatchPattern() {
    super("STRMP");
  }

  @Override
  public boolean compare(String s1, String s2) {
    return s1.matches(s2);
  }
}
