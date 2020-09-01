package me.hsgamer.hscore.expression.string;

import me.hsgamer.hscore.expression.StringComparator;

/**
 * Check if the 1st string is the same as the 2nd string <br> Ex: STREQ("str1", "str1")
 */
public class Equals extends StringComparator {

  public Equals() {
    super("STREQ");
  }

  @Override
  public boolean compare(String s1, String s2) {
    return s1.equals(s2);
  }
}
