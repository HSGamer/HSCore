package me.hsgamer.hscore.utils.expression.string;

import me.hsgamer.hscore.utils.expression.StringComparator;

public class EqualsIgnoreCase extends StringComparator {

  public EqualsIgnoreCase() {
    super("STREQIC");
  }

  @Override
  public boolean compare(String s1, String s2) {
    return s1.equalsIgnoreCase(s2);
  }
}
