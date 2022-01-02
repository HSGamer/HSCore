package me.hsgamer.hscore.expression.number;

import com.udojava.evalex.AbstractFunction;
import com.udojava.evalex.Expression.ExpressionException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Calculate the average number <br> Ex: AVG(1, 2, 3, 4, 5)
 */
public class Average extends AbstractFunction {

  public Average() {
    super("AVG", -1);
  }

  @Override
  @NotNull
  public BigDecimal eval(@NotNull List<BigDecimal> parameters) {
    if (parameters.isEmpty()) {
      throw new ExpressionException("average requires at least one parameter");
    }
    return parameters.stream()
      .reduce(BigDecimal.ZERO, BigDecimal::add)
      .divide(new BigDecimal(parameters.size()), RoundingMode.HALF_EVEN);
  }
}
