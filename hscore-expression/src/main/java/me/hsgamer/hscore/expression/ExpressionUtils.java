package me.hsgamer.hscore.expression;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.parser.ParseException;
import me.hsgamer.hscore.expression.number.Average;
import me.hsgamer.hscore.expression.string.*;

import java.util.Map;
import java.util.Optional;

/**
 * The expression manager
 */
public final class ExpressionUtils {
  private static ExpressionConfiguration expressionConfiguration;

  static {
    expressionConfiguration = ExpressionConfiguration.defaultConfiguration()
      .withAdditionalFunctions(
        Map.entry("AVG", new Average()),
        Map.entry("STRCT", new Contains()),
        Map.entry("STREDW", new EndsWith()),
        Map.entry("STREQ", new Equals()),
        Map.entry("STREQIC", new EqualsIgnoreCase()),
        Map.entry("STRLEN", new Length()),
        Map.entry("STRMP", new MatchPattern()),
        Map.entry("STRSTW", new StartsWith())
      );
  }

  private ExpressionUtils() {

  }

  /**
   * Register a function
   *
   * @param name     the name of the function
   * @param function the function
   */
  public static void registerFunction(String name, FunctionIfc function) {
    expressionConfiguration = expressionConfiguration.withAdditionalFunctions(Map.entry(name, function));
  }

  /**
   * Register an operator
   *
   * @param name     the name of the operator
   * @param operator the operator
   */
  public static void registerOperator(String name, OperatorIfc operator) {
    expressionConfiguration = expressionConfiguration.withAdditionalOperators(Map.entry(name, operator));
  }

  /**
   * Get the expression configuration
   *
   * @return the expression configuration
   */
  public static ExpressionConfiguration getExpressionConfiguration() {
    return expressionConfiguration;
  }

  /**
   * Create an expression
   *
   * @param expression the expression
   *
   * @return the expression
   */
  public static Expression createExpression(String expression) {
    return new Expression(expression, expressionConfiguration);
  }

  /**
   * Evaluate the expression
   *
   * @param expression the expression
   *
   * @return the result
   *
   * @throws EvaluationException occurred when evaluating
   * @throws ParseException      if the expression is invalid
   */
  public static EvaluationValue evaluate(String expression) throws EvaluationException, ParseException {
    return createExpression(expression).evaluate();
  }

  /**
   * Evaluate the expression
   *
   * @param expression the expression
   *
   * @return the result
   */
  public static Optional<EvaluationValue> evaluateSafe(String expression) {
    try {
      return Optional.of(evaluate(expression));
    } catch (EvaluationException | ParseException e) {
      return Optional.empty();
    }
  }

  /**
   * Evaluate the expression
   *
   * @param expression the expression
   * @param values     the values
   *
   * @return the result
   *
   * @throws EvaluationException occurred when evaluating
   * @throws ParseException      if the expression is invalid
   */
  public static EvaluationValue evaluate(String expression, Map<String, Object> values) throws EvaluationException, ParseException {
    return createExpression(expression).withValues(values).evaluate();
  }

  /**
   * Evaluate the expression
   *
   * @param expression the expression
   * @param values     the values
   *
   * @return the result
   */
  public static Optional<EvaluationValue> evaluateSafe(String expression, Map<String, Object> values) {
    try {
      return Optional.of(evaluate(expression, values));
    } catch (EvaluationException | ParseException e) {
      return Optional.empty();
    }
  }
}
