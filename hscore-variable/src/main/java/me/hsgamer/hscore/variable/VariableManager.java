package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The variable manager for the instance
 */
public class VariableManager implements StringReplacer {
  /**
   * The global variable manager
   */
  public static final VariableManager GLOBAL = new VariableManager();

  private final Function<String, VariableSession> sessionFunction;
  private final List<Variable> variableEntries = new ArrayList<>();
  private final List<StringReplacer> externalReplacers = new ArrayList<>();

  /**
   * Create a new variable manager
   *
   * @param sessionFunction the function to create a new {@link VariableSession} from a string
   */
  public VariableManager(Function<String, VariableSession> sessionFunction) {
    this.sessionFunction = sessionFunction;
  }

  /**
   * Create a new variable manager with the default {@link VariableSession} with the user-defined ignore char.
   * The default {@link VariableSession} uses the {@link Pattern} to check for variables with the format <code>{variable}</code>.
   * Developers can add the ignore char to the start and end of the variable to ignore it.
   *
   * @param startIgnoreChar the ignore char at the start of the variable
   * @param endIgnoreChar   the ignore char at the end of the variable
   */
  public VariableManager(char startIgnoreChar, char endIgnoreChar) {
    this(new Function<String, VariableSession>() {
      private final Pattern pattern = Pattern.compile("(.?)([{]([^{}]+)[}])(.?)");

      @Override
      public VariableSession apply(String s) {

        return new VariableSession() {
          private final Matcher matcher = pattern.matcher(s);
          private final StringBuffer stringBuffer = new StringBuffer();

          @Override
          public boolean hasVariable() {
            while (matcher.find()) {
              char startChar = Optional.ofNullable(matcher.group(1)).filter(s -> !s.isEmpty()).map(s -> s.charAt(0)).orElse(' ');
              char endChar = Optional.ofNullable(matcher.group(4)).filter(s -> !s.isEmpty()).map(s -> s.charAt(0)).orElse(' ');
              if (startIgnoreChar == startChar && endIgnoreChar == endChar) {
                String original = matcher.group(2);
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(original));
              } else {
                return true;
              }
            }
            return false;
          }

          @Override
          public String getVariable() {
            return matcher.group(3).trim();
          }

          @Override
          public void replaceVariable(String replacement) {
            String startChar = Optional.ofNullable(matcher.group(1)).filter(s -> !s.isEmpty()).orElse("");
            String endChar = Optional.ofNullable(matcher.group(4)).filter(s -> !s.isEmpty()).orElse("");
            matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(startChar + replacement + endChar));
          }

          @Override
          public String getFinalString() {
            matcher.appendTail(stringBuffer);
            return stringBuffer.toString();
          }
        };
      }
    });
  }

  /**
   * Create a new variable manager with the default {@link VariableSession} with the default ignore char <code>\</code>.
   */
  public VariableManager() {
    this('\\', '\\');
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the replacer
   * @param isWhole  whether the manager should check the whole string matches the prefix, set it to false if you want to check if the prefix is at the beginning of the string
   */
  public void register(String prefix, StringReplacer variable, boolean isWhole) {
    variableEntries.add(new Variable(prefix, isWhole, variable));
  }

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the Variable object
   */
  public void register(String prefix, StringReplacer variable) {
    register(prefix, variable, false);
  }

  /**
   * Unregister a variable
   *
   * @param prefix the prefix
   */
  public void unregister(String prefix) {
    variableEntries.removeIf(entry -> entry.prefix.equalsIgnoreCase(prefix));
  }

  /**
   * Get all variables
   *
   * @return the variables
   */
  public Map<String, StringReplacer> getVariables() {
    return variableEntries.stream().collect(HashMap::new, (map, entry) -> map.put(entry.prefix, entry.replacer), HashMap::putAll);
  }

  /**
   * Get all variable entries
   *
   * @return the variable entries
   */
  public List<Variable> getVariableEntries() {
    return Collections.unmodifiableList(variableEntries);
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
    VariableSession session = sessionFunction.apply(message);
    while (session.hasVariable()) {
      String identifier = session.getVariable();
      variableEntries.stream()
        .filter(entry -> entry.isWhole ? identifier.equalsIgnoreCase(entry.prefix) : identifier.toLowerCase(Locale.ROOT).startsWith(entry.prefix.toLowerCase(Locale.ROOT)))
        .findFirst()
        .map(entry -> entry.replacer.tryReplace(identifier.substring(entry.prefix.length()), uuid))
        .ifPresent(session::replaceVariable);
    }

    message = session.getFinalString();

    for (StringReplacer externalStringReplacer : externalReplacers) {
      String replaced = externalStringReplacer.tryReplace(message, uuid);
      if (replaced != null) {
        message = replaced;
      }
    }

    return message;
  }

  @Override
  public @Nullable String replace(@NotNull String original) {
    return setVariables(original, null);
  }

  @Override
  public @Nullable String replace(@NotNull String original, @NotNull UUID uuid) {
    return setVariables(original, uuid);
  }
}
