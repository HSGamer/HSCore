package me.hsgamer.hscore.expression.expression.string.function;

import me.hsgamer.hscore.expression.expression.StringComparator;

public class Contains extends StringComparator {

  public Contains() {
    super("STRCT");
  }

  @Override
  public boolean compare(String s1, String s2) {
    return s1.contains(s2);
  }
}
