package me.hsgamer.hscore.expression.string;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;

/**
 * Get the length of the string <br> Ex: STRLEN("Hello World")
 */
@FunctionParameter(name = "value")
public class Length extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) {
    return EvaluationValue.numberValue(BigDecimal.valueOf(evaluationValues[0].getStringValue().length()));
  }
}
