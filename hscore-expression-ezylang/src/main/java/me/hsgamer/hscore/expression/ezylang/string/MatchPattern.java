package me.hsgamer.hscore.expression.ezylang.string;

import com.ezylang.evalex.functions.FunctionParameter;
import me.hsgamer.hscore.expression.ezylang.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string matches the RegEx pattern from the 2nd string <br> Ex: STRMP("String",
 * "String-?")
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "pattern")
public class MatchPattern extends StringComparator {
  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.matches(s2);
  }
}
