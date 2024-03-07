package me.hsgamer.hscore.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * A simple interface for String replacement
 */
public interface StringReplacer {
  /**
   * The dummy replacer that does nothing
   */
  StringReplacer DUMMY = original -> null;

  /**
   * Combine multiple string replacers
   *
   * @param stringReplacers the string replacers
   *
   * @return the combined string replacer
   */
  @NotNull
  static StringReplacer combine(@NotNull Collection<? extends StringReplacer> stringReplacers) {
    return new StringReplacer() {
      @Override
      public @NotNull String replace(@NotNull String original) {
        String replaced = original;
        for (StringReplacer replacer : stringReplacers) {
          String newReplaced = replacer.replace(replaced);
          if (newReplaced != null) {
            replaced = newReplaced;
          }
        }
        return replaced;
      }

      @Override
      public @NotNull String replace(@NotNull String original, @NotNull UUID uuid) {
        String replaced = original;
        for (StringReplacer replacer : stringReplacers) {
          String newReplaced = replacer.replace(replaced, uuid);
          if (newReplaced != null) {
            replaced = newReplaced;
          }
        }
        return replaced;
      }
    };
  }

  /**
   * Combine multiple string replacers
   *
   * @param stringReplacers the string replacers
   *
   * @return the combined string replacer
   */
  @NotNull
  static StringReplacer combine(@NotNull StringReplacer... stringReplacers) {
    return combine(Arrays.asList(stringReplacers));
  }

  /**
   * Create a new {@link StringReplacer} from a {@link UnaryOperator} as {@link #replace(String)} and a {@link BiFunction} as {@link #replace(String, UUID)}
   *
   * @param operator the {@link UnaryOperator}
   * @param function the {@link BiFunction}
   *
   * @return the {@link StringReplacer}
   */
  @NotNull
  static StringReplacer of(@NotNull UnaryOperator<String> operator, @NotNull BiFunction<String, UUID, String> function) {
    return new StringReplacer() {
      @Nullable
      @Override
      public String replace(@NotNull String original) {
        return operator.apply(original);
      }

      @Nullable
      @Override
      public String replace(@NotNull String original, @NotNull UUID uuid) {
        return function.apply(original, uuid);
      }
    };
  }

  /**
   * Create a new {@link StringReplacer} from a {@link UnaryOperator} as {@link #replace(String)}
   *
   * @param operator the {@link UnaryOperator}
   *
   * @return the {@link StringReplacer}
   */
  @NotNull
  static StringReplacer of(@NotNull UnaryOperator<String> operator) {
    return operator::apply;
  }

  /**
   * Create a new {@link StringReplacer} from a {@link BiFunction} as {@link #replace(String, UUID)}
   *
   * @param function the {@link BiFunction}
   *
   * @return the {@link StringReplacer}
   */
  @NotNull
  static StringReplacer of(@NotNull BiFunction<String, UUID, String> function) {
    return of(s -> null, function);
  }

  /**
   * Replace a string
   *
   * @param original the original string
   *
   * @return the replaced string
   */
  @Nullable
  String replace(@NotNull String original);

  /**
   * Replace a string based on the unique id
   *
   * @param original the original string
   * @param uuid     the unique id
   *
   * @return the replaced string
   */
  @Nullable
  default String replace(@NotNull String original, @NotNull UUID uuid) {
    return replace(original);
  }

  /**
   * Try to replace a string based on the unique id.
   * If the unique id is null, it will use {@link #replace(String)}. Otherwise, it will use {@link #replace(String, UUID)}.
   *
   * @param original the original string
   * @param uuid     the unique id
   *
   * @return the replaced string, or an empty string if the replaced string is null
   */
  @NotNull
  default String tryReplace(@NotNull String original, @Nullable UUID uuid) {
    if (uuid != null) {
      String replaced = replace(original, uuid);
      if (replaced != null) {
        return replaced;
      }
    }

    String replaced = replace(original);
    return replaced != null ? replaced : "";
  }
}
