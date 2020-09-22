package me.hsgamer.hscore.expression;

import com.udojava.evalex.Expression;
import com.udojava.evalex.LazyFunction;
import com.udojava.evalex.LazyOperator;
import me.hsgamer.hscore.expression.number.Average;
import me.hsgamer.hscore.expression.string.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    lazyFunctionSet.add(new Average());
  }

  private ExpressionUtils() {

  }

  /**
   * Check if the expression is an Boolean expression
   *
   * @param input the expression
   * @return whether it's an Boolean expression
   */
  public static boolean isBoolean(@NotNull String input) {
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
  @Nullable
  public static BigDecimal getResult(@NotNull String input) {
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
  public static boolean isValidExpression(@NotNull String input) {
    return getResult(input) != null;
  }

  /**
   * Apply functions to the expression
   *
   * @param expression the expression
   */
  public static void applyLazyFunction(@NotNull Expression expression) {
    lazyFunctionSet.forEach(expression::addLazyFunction);
  }

  /**
   * Register a function to the expression system
   *
   * @param lazyFunction the function
   */
  public static void registerLazyFunction(@NotNull LazyFunction lazyFunction) {
    lazyFunctionSet.add(lazyFunction);
  }

  /**
   * Apply operators to the expression
   *
   * @param expression the expression
   */
  public static void applyLazyOperator(@NotNull Expression expression) {
    lazyOperatorSet.forEach(expression::addOperator);
  }

  /**
   * Register an operator to the expression system
   *
   * @param lazyOperator the function
   */
  public static void registerLazyOperator(@NotNull LazyOperator lazyOperator) {
    lazyOperatorSet.add(lazyOperator);
  }

  /**
   * Convert to number
   *
   * @param input the string
   * @return the number
   */
  @NotNull
  private static Optional<BigDecimal> getNumber(@NotNull String input) {
    try {
      return Optional.of(new BigDecimal(input));
    } catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }
}
