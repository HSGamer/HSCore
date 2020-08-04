package me.hsgamer.hscore.expression.expression.string.operator;

import com.udojava.evalex.AbstractLazyOperator;
import com.udojava.evalex.Expression;
import com.udojava.evalex.Expression.LazyNumber;
import java.math.BigDecimal;
import me.hsgamer.hscore.expression.OperandValidator;

public class AddOrConcatOperator extends AbstractLazyOperator {

  public AddOrConcatOperator() {
    super("+", Expression.OPERATOR_PRECEDENCE_ADDITIVE, true);
  }

  @Override
  public LazyNumber eval(LazyNumber v1, LazyNumber v2) {
    OperandValidator.notNull(v1, v2);

    BigDecimal b1 = v1.eval();
    BigDecimal b2 = v2.eval();

    return new LazyNumber() {
      @Override
      public BigDecimal eval() {
        if (b1 == null || b2 == null) {
          return null;
        }
        return b1.add(b2);
      }

      @Override
      public String getString() {
        if (b1 == null || b2 == null) {
          return v1.getString() + v2.getString();
        }
        return b1.add(b2).toPlainString();
      }
    };
  }
}
