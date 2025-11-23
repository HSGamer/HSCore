package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link VariableBundle} for common variables
 */
public final class CommonVariableBundle extends VariableBundle {
  /**
   * Create a new bundle for the variable manager
   *
   * @param variableManager the variable manager
   */
  public CommonVariableBundle(VariableManager variableManager) {
    super(variableManager);

    // Random
    register("random_", StringReplacer.of(original -> {
      original = original.trim();
      if (original.contains(":")) {
        String[] split = original.split(":", 2);
        String s1 = split[0].trim();
        String s2 = split[1].trim();
        Optional<Integer> optional1 = Validate.getNumber(s1).map(Number::intValue);
        Optional<Integer> optional2 = Validate.getNumber(s2).map(Number::intValue);
        if (optional1.isPresent() && optional2.isPresent()) {
          int i1 = optional1.get();
          int i2 = optional2.get();
          int max = Math.max(i1, i2);
          int min = Math.min(i1, i2);
          return String.valueOf(ThreadLocalRandom.current().nextInt(min, max + 1));
        }
      } else {
        Optional<Integer> optional = Validate.getNumber(original).map(Number::intValue);
        if (optional.isPresent()) {
          return String.valueOf(ThreadLocalRandom.current().nextInt(optional.get()));
        }
      }
      return null;
    }));

    // UUID
    register("uuid", StringReplacer.of((original, uuid) -> uuid.toString()), true);
  }
}
