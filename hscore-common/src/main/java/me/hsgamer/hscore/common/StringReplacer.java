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
   * Replace a string based on the unique id
   *
   * @param original        the original string
   * @param uuid            the unique id
   * @param stringReplacers the list of string replacer
   *
   * @return the replaced string
   */
  @NotNull
  static String replace(@NotNull String original, @Nullable UUID uuid, @NotNull Collection<? extends StringReplacer> stringReplacers) {
    String replaced = original;
    for (StringReplacer replacer : stringReplacers) {
      String newReplaced = replacer.tryReplace(replaced, uuid);
      if (newReplaced != null) {
        replaced = newReplaced;
      }
    }
    return replaced;
  }

  /**
   * Replace a string based on the unique id
   *
   * @param original        the original string
   * @param uuid            the unique id
   * @param stringReplacers the list of string replacer
   *
   * @return the replaced string
   */
  @NotNull
  static String replace(@NotNull String original, @Nullable UUID uuid, @NotNull StringReplacer... stringReplacers) {
    return replace(original, uuid, Arrays.asList(stringReplacers));
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
   * @return the replaced string
   */
  @Nullable
  default String tryReplace(@NotNull String original, @Nullable UUID uuid) {
    return uuid == null ? replace(original) : replace(original, uuid);
  }
}
