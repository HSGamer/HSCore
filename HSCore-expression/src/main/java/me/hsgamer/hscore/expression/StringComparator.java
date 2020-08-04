package me.hsgamer.hscore.expression;

import com.udojava.evalex.AbstractLazyFunction;
import com.udojava.evalex.Expression.LazyNumber;
import java.util.List;

public abstract class StringComparator extends AbstractLazyFunction {

  public StringComparator(String name) {
    super(name, 2, true);
  }

  public abstract boolean compare(String s1, String s2);

  @Override
  public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
    return BooleanLazyNumber
        .convert(compare(lazyParams.get(0).getString(), lazyParams.get(1).getString()));
  }
}
