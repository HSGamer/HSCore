package me.hsgamer.hscore.expression.expression.string.operator;

import com.udojava.evalex.Expression.LazyNumber;
import me.hsgamer.hscore.expression.BooleanLazyNumber;

public class NotEqualsOperator extends EqualsOperator {

  public NotEqualsOperator(String oper) {
    super(oper);
  }

  @Override
  public LazyNumber eval(LazyNumber v1, LazyNumber v2) {
    return BooleanLazyNumber.invert(super.eval(v1, v2));
  }
}
