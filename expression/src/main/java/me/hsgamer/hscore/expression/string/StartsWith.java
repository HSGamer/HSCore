package me.hsgamer.hscore.expression.string;

import com.ezylang.evalex.functions.FunctionParameter;
import me.hsgamer.hscore.expression.StringComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Check if the 1st string starts with the 2nd string <br> Ex: STRSTW("String", "Str")
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "substring")
public class StartsWith extends StringComparator {
  @Override
  public boolean compare(@NotNull String s1, @NotNull String s2) {
    return s1.startsWith(s2);
  }
}
