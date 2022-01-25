package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.interfaces.StringReplacer;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Variable Manager
 */
public final class VariableManager {
  private static final Pattern PATTERN = Pattern.compile("(.)([{]([^{}]+)[}])(.)");
  private static final String START_IGNORE_CHAR = "\\";
  private static final String END_IGNORE_CHAR = "\\";
  private static final Map<String, StringReplacer> variables = new HashMap<>();
  private static final List<ExternalStringReplacer> externalReplacers = new ArrayList<>();
  private static BooleanSupplier replaceAll = () -> false;

  private VariableManager() {
    // EMPTY
  }

  /**
   * Whether the manager should replace all similar variables on single time
   *
   * @param replaceAll true if it should
   *
   * @see #setSingleVariables(String, UUID)
   */
  public static void setReplaceAll(boolean replaceAll) {
    setReplaceAll(() -> replaceAll);
  }

  /**
   * Whether the manager should replace all similar variables on single time
   *
   * @param replaceAll the boolean supplier (true if it should)
   *
   * @see #setSingleVariables(String, UUID)
   */
  public static void setReplaceAll(BooleanSupplier replaceAll) {
    VariableManager.replaceAll = replaceAll;
  }

  /**
   * Add an external replacer
   *
   * @param replacer the external string replacer
   */
  public static void addExternalReplacer(ExternalStringReplacer replacer) {
    externalReplacers.add(replacer);
  }

  /**
   * Clear all external replacers
   */
  public static void clearExternalReplacers() {
    externalReplacers.clear();
  }

  /**
   * Get all external replacers
   *
   * @return the external replacers
   */
  public static List<ExternalStringReplacer> getExternalReplacers() {
    return Collections.unmodifiableList(externalReplacers);
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the Variable object
   */
  public static void register(String prefix, StringReplacer variable) {
    variables.put(prefix, variable);
  }

  /**
   * Unregister a variable
   *
   * @param prefix the prefix
   */
  public static void unregister(String prefix) {
    variables.remove(prefix);
  }

  /**
   * Get all variables
   *
   * @return the variables
   */
  public static Map<String, StringReplacer> getVariables() {
    return Collections.unmodifiableMap(variables);
  }

  /**
   * Check if a string contains variables
   *
   * @param message the string
   *
   * @return true if it has, otherwise false
   */
  public static boolean hasVariables(String message) {
    if (message == null || message.trim().isEmpty()) {
      return false;
    }
    if (isMatch(message)) {
      return true;
    }
    return externalReplacers.parallelStream().anyMatch(replacer -> replacer.canBeReplaced(message));
  }

  /**
   * Replace the variables of the string until it cannot be replaced anymore
   *
   * @param message the string
   * @param uuid    the unique id
   *
   * @return the replaced string
   */
  public static String setVariables(String message, UUID uuid) {
    if (!hasVariables(message)) {
      return message;
    }
    String old;
    do {
      old = message;
      message = setSingleVariables(message, uuid);
    } while (hasVariables(message) && !old.equals(message));
    return message;
  }

  /**
   * Replace the variables of the string (single time)
   *
   * @param message the string
   * @param uuid    the unique id
   *
   * @return the replaced string
   */
  public static String setSingleVariables(String message, UUID uuid) {
    Matcher matcher = PATTERN.matcher(message);
    while (matcher.find()) {
      String startChar = matcher.group(1);
      String endChar = matcher.group(4);
      String original = matcher.group(2);
      if (START_IGNORE_CHAR.equals(startChar) && END_IGNORE_CHAR.equals(endChar)) {
        message = message.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(original));
        continue;
      }

      String identifier = matcher.group(3).trim();
      for (Map.Entry<String, ? extends StringReplacer> variable : variables.entrySet()) {
        if (!identifier.startsWith(variable.getKey())) {
          continue;
        }

        String replace = variable.getValue().replace(identifier.substring(variable.getKey().length()), uuid);
        if (replace == null) {
          continue;
        }

        if (replaceAll.getAsBoolean()) {
          message = message.replaceAll(Pattern.quote(original), Matcher.quoteReplacement(replace));
        } else {
          message = message.replaceFirst(Pattern.quote(original), Matcher.quoteReplacement(replace));
        }
      }
    }
    for (ExternalStringReplacer externalStringReplacer : externalReplacers) {
      message = externalStringReplacer.replace(message, uuid);
    }
    return message;
  }

  /**
   * Check if the string contains valid variables
   *
   * @param string the string
   *
   * @return true if it does
   */
  private static boolean isMatch(String string) {
    Matcher matcher = PATTERN.matcher(string);
    List<String> found = new ArrayList<>();
    while (matcher.find()) {
      found.add(matcher.group(3).trim());
    }

    if (found.isEmpty()) {
      return false;
    } else {
      return found.stream().parallel().anyMatch(s -> {
        for (String match : variables.keySet()) {
          if (s.startsWith(match)) {
            return true;
          }
        }
        return false;
      });
    }
  }
}
