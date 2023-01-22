package me.hsgamer.hscore.expression;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.parser.Token;
import org.jetbrains.annotations.NotNull;

/**
 * The String Comparator
 */
public abstract class StringComparator extends AbstractFunction {
  /**
   * Compare the two strings
   *
   * @param s1 the 1st string
   * @param s2 the 2nd string
   *
   * @return the result
   */
  public abstract boolean compare(@NotNull String s1, @NotNull String s2);

  @Override
  public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) {
    return new EvaluationValue(compare(evaluationValues[0].getStringValue(), evaluationValues[1].getStringValue()));
  }
}
