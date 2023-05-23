package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The utility class to register common variables to the {@link VariableBundle}
 */
public final class CommonVariableBundle {
  private CommonVariableBundle() {
    // EMPTY
  }

  /**
   * Register the common variables to the {@link VariableBundle}
   *
   * @param bundle the bundle
   */
  public static void registerVariables(VariableBundle bundle) {
    // Random
    bundle.register("random_", StringReplacer.of(original -> {
      original = original.trim();
      if (original.contains(":")) {
        String[] split = original.split(":", 2);
        String s1 = split[0].trim();
        String s2 = split[1].trim();
        if (Validate.isValidInteger(s1) && Validate.isValidInteger(s2)) {
          int i1 = Integer.parseInt(s1);
          int i2 = Integer.parseInt(s2);
          int max = Math.max(i1, i2);
          int min = Math.min(i1, i2);
          return String.valueOf(ThreadLocalRandom.current().nextInt(min, max + 1));
        }
      } else if (Validate.isValidInteger(original)) {
        return String.valueOf(ThreadLocalRandom.current().nextInt(Integer.parseInt(original)));
      }
      return null;
    }));

    // UUID
    bundle.register("uuid", StringReplacer.of((original, uuid) -> uuid.toString()), true);
  }
}
