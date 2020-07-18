package me.hsgamer.hscore.utils.expression.string;

import me.hsgamer.hscore.utils.expression.StringComparator;

public class StartsWith extends StringComparator {

  public StartsWith() {
    super("STRSTW");
  }

  @Override
  public boolean compare(String s1, String s2) {
    return s1.startsWith(s2);
  }
}
