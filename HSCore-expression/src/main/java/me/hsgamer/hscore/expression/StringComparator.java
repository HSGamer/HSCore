package me.hsgamer.hscore.expression;

import com.udojava.evalex.AbstractLazyFunction;
import com.udojava.evalex.Expression.LazyNumber;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The String Comparator
 */
public abstract class StringComparator extends AbstractLazyFunction {

  /**
   * Create new comparator
   *
   * @param name the prefix of the comparator
   */
  public StringComparator(@NotNull String name) {
    super(name, 2, true);
  }

  /**
   * Compare the two strings
   *
   * @param s1 the 1st string
   * @param s2 the 2nd string
   *
   * @return the result
   */
  public abstract boolean compare(@NotNull String s1, @NotNull String s2);

  @Override
  @NotNull
  public LazyNumber lazyEval(@NotNull List<LazyNumber> lazyParams) {
    return BooleanLazyNumber
      .convert(compare(lazyParams.get(0).getString(), lazyParams.get(1).getString()));
  }
}
