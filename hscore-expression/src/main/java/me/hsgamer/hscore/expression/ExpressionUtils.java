package me.hsgamer.hscore.expression;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.operators.OperatorIfc;
import me.hsgamer.hscore.expression.number.Average;
import me.hsgamer.hscore.expression.string.*;

import java.util.Map;
import java.util.function.Function;

/**
 * The expression manager
 */
public final class ExpressionUtils {
  private static Function<ExpressionConfiguration, ExpressionConfiguration> expressionConfigurationModifier;

  static {
    expressionConfigurationModifier = configuration ->
      configuration.withAdditionalFunctions(
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
    expressionConfigurationModifier = expressionConfigurationModifier.andThen(configuration -> configuration.withAdditionalFunctions(Map.entry(name, function)));
  }

  /**
   * Register an operator
   *
   * @param name     the name of the operator
   * @param operator the operator
   */
  public static void registerOperator(String name, OperatorIfc operator) {
    expressionConfigurationModifier = expressionConfigurationModifier.andThen(configuration -> configuration.withAdditionalOperators(Map.entry(name, operator)));
  }

  /**
   * Get the expression configuration modifier
   *
   * @return the expression configuration modifier
   */
  public static Function<ExpressionConfiguration, ExpressionConfiguration> getExpressionConfigurationModifier() {
    return expressionConfigurationModifier;
  }

  /**
   * Get the default expression configuration
   *
   * @return the expression configuration
   */
  public static ExpressionConfiguration getDefaultExpressionConfiguration() {
    return expressionConfigurationModifier.apply(ExpressionConfiguration.defaultConfiguration());
  }

  /**
   * Apply the modifier to the expression configuration
   *
   * @param configuration the configuration
   *
   * @return the modified configuration
   */
  public static ExpressionConfiguration applyExpressionConfigurationModifier(ExpressionConfiguration configuration) {
    return expressionConfigurationModifier.apply(configuration);
  }
}
