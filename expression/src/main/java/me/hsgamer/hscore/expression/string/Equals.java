package me.hsgamer.hscore.expression.string;

import com.ezylang.evalex.functions.FunctionParameter;
import me.hsgamer.hscore.expression.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string is the same as the 2nd string <br> Ex: STREQ("str1", "str1")
 */
@FunctionParameter(name = "first")
@FunctionParameter(name = "second")
public class Equals extends StringComparator {
  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.equals(s2);
  }
}
