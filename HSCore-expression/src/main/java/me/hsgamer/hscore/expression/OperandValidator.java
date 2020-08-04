package me.hsgamer.hscore.expression;

import com.udojava.evalex.Expression.LazyNumber;
import java.math.BigDecimal;

public class OperandValidator {

  private OperandValidator() {

  }

  /**
   * Check if the operands are not null
   *
   * @param operands the operands
   */
  public static void notNull(LazyNumber... operands) {
    for (int i = 0; i < operands.length; i++) {
      if (operands[i] == null) {
        throw new ArithmeticException("Operand at index " + i + " may not be null");
      }
    }
  }

  /**
   * Check if the operands are not null
   *
   * @param operands the operands
   */
  public static void notNull(BigDecimal... operands) {
    for (int i = 0; i < operands.length; i++) {
      if (operands[i] == null) {
        throw new ArithmeticException("Operand at index " + i + " may not be null");
      }
    }
  }
}
