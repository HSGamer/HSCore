package me.hsgamer.hscore.expression.expression.string.operator;

import com.udojava.evalex.AbstractLazyOperator;
import com.udojava.evalex.Expression;
import com.udojava.evalex.Expression.LazyNumber;
import me.hsgamer.hscore.expression.BooleanLazyNumber;

public class EqualsOperator extends AbstractLazyOperator {

  public EqualsOperator(String oper) {
    super(oper, Expression.OPERATOR_PRECEDENCE_EQUALITY, false, true);
  }

  @Override
  public LazyNumber eval(LazyNumber v1, LazyNumber v2) {
    if (v1 == v2) {
      return BooleanLazyNumber.TRUE;
    }
    if (v1 == null || v2 == null) {
      return BooleanLazyNumber.FALSE;
    }
    if (v1.eval() == null || v2.eval() == null) {
      return BooleanLazyNumber.convert(v1.getString().equals(v2.getString()));
    }
    return BooleanLazyNumber.convert(v1.eval().compareTo(v2.eval()) == 0);
  }
}
