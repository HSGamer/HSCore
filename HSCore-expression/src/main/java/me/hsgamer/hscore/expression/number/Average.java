package me.hsgamer.hscore.expression.number;

import com.udojava.evalex.AbstractFunction;
import com.udojava.evalex.Expression.ExpressionException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Calculate the average number <br> Ex: AVG(1, 2, 3, 4, 5)
 */
public class Average extends AbstractFunction {

  public Average() {
    super("AVG", -1);
  }

  @Override
  public BigDecimal eval(List<BigDecimal> parameters) {
    if (parameters.isEmpty()) {
      throw new ExpressionException("average requires at least one parameter");
    }
    return parameters.stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .divide(new BigDecimal(parameters.size()));
  }
}
