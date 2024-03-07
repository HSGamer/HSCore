package me.hsgamer.hscore.bukkit.utils;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.StringUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

/**
 * Methods to colorize strings
 */
public final class ColorUtils {
  private static final Pattern hybridColorPattern = Pattern.compile("(\\\\?)((\\S)\\(#([A-Fa-f\\d]{1,6}),([a-zA-Z\\d])\\))");
  private static final Map<Character, Supplier<Character>> colorMappers = Collections.singletonMap('u', () -> getRandomColor().getChar());
  private static BooleanSupplier hexSupportGlobalCheck = () -> VersionUtils.isAtLeast(16);

  private ColorUtils() {
    // EMPTY
  }

  /**
   * Replace the hybrid color format to the final color.
   * The format is "?(#[A-Fa-f\\d]{1,6},[a-zA-Z\\d])", where ? is the indicator character and (#[A-Fa-f\\d]{1,6},[a-zA-Z\\d]) is the code.
   *
   * @param indicator the indicator character
   * @param input     the input string
   *
   * @return the converted string
   */
  @NotNull
  public static String replaceHybridColorCode(final char indicator, final String input) {
    return StringUtils.replacePattern(input, hybridColorPattern, matcher -> {
      String matchedChar = matcher.group(3);
      if (matchedChar.indexOf(indicator) < 0) {
        return null;
      }
      boolean skip = matcher.group(1).equals("\\");
      if (skip) {
        return matcher.group(2);
      } else if (hexSupportGlobalCheck.getAsBoolean()) {
        return indicator + "#" + matcher.group(4);
      } else {
        return indicator + matcher.group(5);
      }
    });
  }

  /**
   * Convert to colored string
   *
   * @param input the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorize(@NotNull final String input) {
    char colorChar = '&';
    return colorize(colorChar, colorizeHex(colorChar, replaceHybridColorCode(colorChar, input)));
  }

  /**
   * Convert HEX string to color.
   * The HEX string format is "?#rrggbb", where "?" is the alternative color char
   *
   * @param altColorChar the alternative color char
   * @param input        the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorizeHex(final char altColorChar, @NotNull final String input) {
    return StringUtils.replaceHex(altColorChar, hex ->
        COLOR_CHAR + "x"
          + COLOR_CHAR + hex[0] + COLOR_CHAR + hex[1]
          + COLOR_CHAR + hex[2] + COLOR_CHAR + hex[3]
          + COLOR_CHAR + hex[4] + COLOR_CHAR + hex[5],
      input);
  }

  /**
   * Convert to colored string
   *
   * @param altColorChar the alternative color char
   * @param input        the string
   *
   * @return the colored string
   */
  @NotNull
  public static String colorize(final char altColorChar, @NotNull final String input) {
    return StringUtils.replaceChar(altColorChar, COLOR_CHAR, input, colorMappers);
  }

  /**
   * Get a random chat color
   *
   * @return the chat color
   */
  @NotNull
  public static ChatColor getRandomColor() {
    return Objects.requireNonNull(CollectionUtils.pickRandom(ChatColor.values(), ChatColor::isColor));
  }

  /**
   * Set the global predicate that checks if the server supports hex colors
   *
   * @param hexSupportGlobalCheck the predicate
   */
  public static void setHexSupportGlobalCheck(BooleanSupplier hexSupportGlobalCheck) {
    ColorUtils.hexSupportGlobalCheck = hexSupportGlobalCheck;
  }
}
