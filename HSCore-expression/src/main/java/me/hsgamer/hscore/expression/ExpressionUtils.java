package me.hsgamer.hscore.expression;

import com.udojava.evalex.Expression;
import com.udojava.evalex.LazyFunction;
import com.udojava.evalex.LazyOperator;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import me.hsgamer.hscore.expression.string.Contains;
import me.hsgamer.hscore.expression.string.EndsWith;
import me.hsgamer.hscore.expression.string.Equals;
import me.hsgamer.hscore.expression.string.EqualsIgnoreCase;
import me.hsgamer.hscore.expression.string.Length;
import me.hsgamer.hscore.expression.string.MatchPattern;
import me.hsgamer.hscore.expression.string.StartsWith;

/**
 * The expression manager
 */
public final class ExpressionUtils {

  private static final Set<LazyFunction> lazyFunctionSet = new HashSet<>();
  private static final Set<LazyOperator> lazyOperatorSet = new HashSet<>();

  static {
    lazyFunctionSet.add(new Equals());
    lazyFunctionSet.add(new EqualsIgnoreCase());
    lazyFunctionSet.add(new Contains());
    lazyFunctionSet.add(new StartsWith());
    lazyFunctionSet.add(new EndsWith());
    lazyFunctionSet.add(new Length());
    lazyFunctionSet.add(new MatchPattern());
  }

  private ExpressionUtils() {

  }

  /**
   * Check if the expression is an Boolean expression
   *
   * @param input the expression
   * @return whether it's an Boolean expression
   */
  public static boolean isBoolean(String input) {
    Expression expression = new Expression(input);
    applyLazyFunction(expression);
    applyLazyOperator(expression);
    try {
      return expression.isBoolean();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Get the result of the expression
   *
   * @param input the expression
   * @return the result
   */
  public static BigDecimal getResult(String input) {
    Optional<BigDecimal> number = getNumber(input);
    if (number.isPresent()) {
      return number.get();
    }

    Expression expression = new Expression(input);
    applyLazyFunction(expression);
    applyLazyOperator(expression);
    try {
      return expression.eval();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Check if it's a valid expression
   *
   * @param input the expression
   * @return whether it's valid
   */
  public static boolean isValidExpression(String input) {
    return getResult(input) != null;
  }

  private static void applyLazyFunction(Expression expression) {
    lazyFunctionSet.forEach(expression::addLazyFunction);
  }

  /**
   * Register a function to the expression system
   *
   * @param lazyFunction the function
   */
  public static void registerLazyFunction(LazyFunction lazyFunction) {
    lazyFunctionSet.add(lazyFunction);
  }

  private static void applyLazyOperator(Expression expression) {
    lazyOperatorSet.forEach(expression::addOperator);
  }

  /**
   * Register an operator to the expression system
   *
   * @param lazyOperator the function
   */
  public static void registerLazyOperator(LazyOperator lazyOperator) {
    lazyOperatorSet.add(lazyOperator);
  }

  /**
   * Convert to number
   *
   * @param input the string
   * @return the number
   */
  private static Optional<BigDecimal> getNumber(String input) {
    try {
      return Optional.of(new BigDecimal(input));
    } catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }
}
