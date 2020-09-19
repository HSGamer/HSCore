package me.hsgamer.hscore.expression;

import com.udojava.evalex.Expression.LazyNumber;

import java.math.BigDecimal;

public class BooleanLazyNumber {

  /**
   * The FALSE state for Boolean LazyFunction and LazyOperator
   */
  public static final LazyNumber FALSE = new LazyNumber() {
    public BigDecimal eval() {
      return BigDecimal.ZERO;
    }

    public String getString() {
      return "0";
    }
  };
  /**
   * The TRUE state for Boolean LazyFunction and LazyOperator
   */
  public static final LazyNumber TRUE = new LazyNumber() {
    public BigDecimal eval() {
      return BigDecimal.ONE;
    }

    public String getString() {
      return "1";
    }
  };

  private BooleanLazyNumber() {

  }

  /**
   * Convert primary boolean to BooleanLazyNumber
   *
   * @param bool the primary boolean
   * @return BooleanLazyNumber's boolean
   */
  public static LazyNumber convert(boolean bool) {
    return bool ? TRUE : FALSE;
  }

  /**
   * Invert the boolean state of BooleanLazyNumber
   *
   * @param input the original state
   * @return the inverted state
   */
  public static LazyNumber invert(LazyNumber input) {
    return input == TRUE ? FALSE : TRUE;
  }
}
