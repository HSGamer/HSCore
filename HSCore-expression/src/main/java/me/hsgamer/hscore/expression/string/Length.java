package me.hsgamer.hscore.expression.string;

import com.udojava.evalex.AbstractLazyFunction;
import com.udojava.evalex.Expression.LazyNumber;
import java.math.BigDecimal;
import java.util.List;

/**
 * Get the length of the string <br> Ex: STRLEN("Hello World")
 */
public class Length extends AbstractLazyFunction {

  public Length() {
    super("STRLEN", 1, false);
  }

  @Override
  public LazyNumber lazyEval(List<LazyNumber> list) {
    int len = list.get(0).getString().length();
    return new LazyNumber() {
      @Override
      public BigDecimal eval() {
        return BigDecimal.valueOf(len);
      }

      @Override
      public String getString() {
        return String.valueOf(len);
      }
    };
  }
}
