package me.hsgamer.hscore.expression.number;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Calculate the average number <br> Ex: AVG(1, 2, 3, 4, 5)
 */
@FunctionParameter(name = "value", isVarArg = true)
public class Average extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) {
    BigDecimal averageNumber = Arrays.stream(evaluationValues)
      .map(EvaluationValue::getNumberValue)
      .reduce(BigDecimal.ZERO, BigDecimal::add)
      .divide(new BigDecimal(evaluationValues.length), expression.getConfiguration().getMathContext().getRoundingMode());
    return new EvaluationValue(averageNumber);
  }
}
