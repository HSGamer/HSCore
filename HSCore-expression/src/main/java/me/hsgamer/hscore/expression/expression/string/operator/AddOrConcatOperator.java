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

    return new LazyNumber() {
      @Override
      public BigDecimal eval() {
        if (v1.eval() == null || v2.eval() == null) {
          return null;
        }
        return v1.eval().add(v2.eval());
      }

      @Override
      public String getString() {
        if (v1.eval() == null || v2.eval() == null) {
          return v1.getString() + v2.getString();
        }
        return v1.eval().add(v2.eval()).toPlainString();
      }
    };
  }
}
