package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;

public class Equals extends StringComparator {

  public Equals() {
    super("STREQ");
  }

  @Override
  public boolean compare(String s1, String s2) {
    return s1.equals(s2);
  }
}
