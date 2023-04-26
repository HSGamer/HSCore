package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The variable manager for the instance
 */
public class VariableManager {
  /**
   * The global variable manager
   */
  public static final VariableManager GLOBAL = new VariableManager();

  private static final Pattern PATTERN = Pattern.compile("(.?)([{]([^{}]+)[}])(.?)");
  private static final char START_IGNORE_CHAR = '\\';
  private static final char END_IGNORE_CHAR = '\\';
  private final Map<String, StringReplacer> variables = new HashMap<>();
  private final List<StringReplacer> externalReplacers = new ArrayList<>();
  private BooleanSupplier replaceAll = () -> false;

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the Variable object
   */
  public void register(String prefix, StringReplacer variable) {
    variables.put(prefix, variable);
  }

  /**
   * Unregister a variable
   *
   * @param prefix the prefix
   */
  public void unregister(String prefix) {
    variables.remove(prefix);
  }

  /**
   * Get all variables
   *
   * @return the variables
   */
  public Map<String, StringReplacer> getVariables() {
    return Collections.unmodifiableMap(variables);
  }

  /**
   * Add an external replacer
   *
   * @param replacer the external string replacer
   */
  public void addExternalReplacer(StringReplacer replacer) {
    externalReplacers.add(replacer);
  }

  /**
   * Remove an external replacer
   *
   * @param replacer the external string replacer
   */
  public void removeExternalReplacer(StringReplacer replacer) {
    externalReplacers.remove(replacer);
  }

  /**
   * Clear all external replacers
   */
  public void clearExternalReplacers() {
    externalReplacers.clear();
  }

  /**
   * Get all external replacers
   *
   * @return the external replacers
   */
  public List<StringReplacer> getExternalReplacers() {
    return Collections.unmodifiableList(externalReplacers);
  }

  /**
   * Replace the variables of the string until it cannot be replaced anymore
   *
   * @param message the string
   * @param uuid    the unique id
   *
   * @return the replaced string
   */
  @NotNull
  public String setVariables(@NotNull String message, @Nullable UUID uuid) {
    String old;
    do {
      old = message;
      message = setSingleVariables(message, uuid);
      for (StringReplacer externalStringReplacer : externalReplacers) {
        String replaced = externalStringReplacer.tryReplace(message, uuid);
        if (replaced != null) {
          message = replaced;
        }
      }
    } while (!old.equals(message));
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
  @NotNull
  public String setSingleVariables(@NotNull String message, @Nullable UUID uuid) {
    Matcher matcher = PATTERN.matcher(message);
    while (matcher.find()) {
      char startChar = Optional.ofNullable(matcher.group(1)).filter(s -> !s.isEmpty()).map(s -> s.charAt(0)).orElse(' ');
      char endChar = Optional.ofNullable(matcher.group(4)).filter(s -> !s.isEmpty()).map(s -> s.charAt(0)).orElse(' ');
      String original = matcher.group(2);
      if (START_IGNORE_CHAR == startChar && END_IGNORE_CHAR == endChar) {
        message = message.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(original));
        continue;
      }

      String identifier = matcher.group(3).trim();
      for (Map.Entry<String, StringReplacer> variable : variables.entrySet()) {
        if (!identifier.startsWith(variable.getKey())) {
          continue;
        }

        String parameter = identifier.substring(variable.getKey().length());
        String replace = variable.getValue().tryReplace(parameter, uuid);
        if (replace == null) {
          continue;
        }

        if (this.getReplaceAll()) {
          message = message.replaceAll(Pattern.quote(original), Matcher.quoteReplacement(replace));
        } else {
          message = message.replaceFirst(Pattern.quote(original), Matcher.quoteReplacement(replace));
        }
      }
    }
    return message;
  }

  /**
   * Get the status whether the manager should replace all similar variables on single time
   *
   * @return true if it should
   *
   * @see #setSingleVariables(String, UUID)
   */
  public boolean getReplaceAll() {
    return replaceAll.getAsBoolean();
  }

  /**
   * Whether the manager should replace all similar variables on single time
   *
   * @param replaceAll true if it should
   *
   * @see #setSingleVariables(String, UUID)
   */
  public void setReplaceAll(boolean replaceAll) {
    setReplaceAll(() -> replaceAll);
  }

  /**
   * Whether the manager should replace all similar variables on single time
   *
   * @param replaceAll the boolean supplier (true if it should)
   *
   * @see #setSingleVariables(String, UUID)
   */
  public void setReplaceAll(BooleanSupplier replaceAll) {
    this.replaceAll = replaceAll;
  }
}
