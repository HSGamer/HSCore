package me.hsgamer.hscore.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methods for String
 */
public final class StringUtils {

  private static final Pattern hexPattern = Pattern.compile("(\\\\?)((\\S)#([A-Fa-f\\d]{1,6}))");

  private StringUtils() {
    // EMPTY
  }

  /**
   * Replace the string given the pattern and the replacement function
   *
   * @param input               the input string
   * @param pattern             the pattern
   * @param replacementFunction the replacement function
   *
   * @return the replaced string
   */
  public static String replacePattern(@NotNull final String input, @NotNull final Pattern pattern, @NotNull final Function<@NotNull Matcher, @Nullable String> replacementFunction) {
    Matcher matcher = pattern.matcher(input);
    if (!matcher.find()) {
      return input;
    }
    StringBuffer buffer = new StringBuffer(input.length() + 4 * 8);
    do {
      String replacement = replacementFunction.apply(matcher);
      if (replacement != null) {
        matcher.appendReplacement(buffer, replacement);
      }
    } while (matcher.find());
    matcher.appendTail(buffer);
    return buffer.toString();
  }

  /**
   * Replace all alternative character format to the final characters.
   * The format is "?&lt;a-zA-Z0-9&gt;", where ? is the alternative character and &lt;a-zA-Z0-9&gt; is the code.
   *
   * @param altChar     The alternative character
   * @param finalChar   The final character
   * @param input       The input string
   * @param charMappers The character mappers to replace some special characters
   *
   * @return The converted string
   */
  @NotNull
  public static String replaceChar(final char altChar, final char finalChar, @NotNull final String input, @NotNull final Map<Character, Supplier<Character>> charMappers) {
    if (input.indexOf(altChar) < 0) {
      return input;
    }
    char[] chars = input.toCharArray();
    int inputIndex = 0;
    int outputIndex = 0;
    while (inputIndex < chars.length - 1) {
      if (chars[inputIndex] == altChar && Character.isLetterOrDigit(chars[inputIndex + 1])) {
        chars[outputIndex++] = finalChar;
        char currentChar = Character.toLowerCase(chars[++inputIndex]);
        if (charMappers.containsKey(currentChar)) {
          chars[outputIndex++] = charMappers.get(currentChar).get();
        } else {
          chars[outputIndex++] = currentChar;
        }
        if (inputIndex + 1 < chars.length && Character.isWhitespace(chars[inputIndex + 1])) {
          inputIndex++;
        }
      } else if (chars[inputIndex] == '\\' && chars[inputIndex + 1] == altChar) {
        chars[outputIndex++] = chars[++inputIndex];
      } else {
        chars[outputIndex++] = chars[inputIndex];
      }
      inputIndex++;
    }
    if (inputIndex == chars.length - 1) {
      chars[outputIndex++] = chars[inputIndex];
    }
    return new String(chars, 0, Math.min(outputIndex, chars.length));
  }

  /**
   * Replace HEX string with the replacer.
   * The HEX string format is "?#rrggbb", where "?" is the indicator character, and "rrggbb" is the HEX string.
   *
   * @param indicator the indicator character
   * @param replacer  the replacer to replace the HEX string, which is a character array of length 6
   * @param input     the string
   *
   * @return the colored string
   */
  @NotNull
  public static String replaceHex(final char indicator, @NotNull final Function<char[], String> replacer, @NotNull final String input) {
    return replacePattern(input, hexPattern, matcher -> {
      String matchedChar = matcher.group(3);
      if (matchedChar.indexOf(indicator) < 0) {
        return null;
      }
      boolean skip = matcher.group(1).equals("\\");
      if (skip) {
        return matcher.group(2);
      } else {
        char[] hex = normalizeHex(matcher.group(4));
        return replacer.apply(hex);
      }
    });
  }

  /**
   * Normalize the raw hex string to the 6-digit hex string
   *
   * @param input the raw hex string
   *
   * @return the 6-digit hex string
   */
  public static char[] normalizeHex(@NotNull final String input) {
    char[] chars = new char[6];
    switch (input.length()) {
      case 0:
        chars[0] = '0';
        chars[1] = '0';
        chars[2] = '0';
        chars[3] = '0';
        chars[4] = '0';
        chars[5] = '0';
        break;
      case 1:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(0);
        chars[2] = input.charAt(0);
        chars[3] = input.charAt(0);
        chars[4] = input.charAt(0);
        chars[5] = input.charAt(0);
        break;
      case 2:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(1);
        chars[2] = input.charAt(0);
        chars[3] = input.charAt(1);
        chars[4] = input.charAt(0);
        chars[5] = input.charAt(1);
        break;
      case 3:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(0);
        chars[2] = input.charAt(1);
        chars[3] = input.charAt(1);
        chars[4] = input.charAt(2);
        chars[5] = input.charAt(2);
        break;
      case 4:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(0);
        chars[2] = input.charAt(1);
        chars[3] = input.charAt(1);
        chars[4] = input.charAt(2);
        chars[5] = input.charAt(3);
        break;
      case 5:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(1);
        chars[2] = input.charAt(2);
        chars[3] = input.charAt(3);
        chars[4] = '0';
        chars[5] = input.charAt(4);
        break;
      default:
        chars[0] = input.charAt(0);
        chars[1] = input.charAt(1);
        chars[2] = input.charAt(2);
        chars[3] = input.charAt(3);
        chars[4] = input.charAt(4);
        chars[5] = input.charAt(5);
        break;
    }
    for (int i = 0; i < chars.length; i++) {
      chars[i] = Character.toLowerCase(chars[i]);
      if (!((chars[i] >= '0' && chars[i] <= '9') || (chars[i] >= 'a' && chars[i] <= 'f'))) {
        chars[i] = '0';
      }
    }
    return chars;
  }
}
