package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.StringReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BooleanSupplier;
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

  private static final Pattern PATTERN = Pattern.compile("(.?)([{]([^{}]+)[}])(.?)");
  private static final char START_IGNORE_CHAR = '\\';
  private static final char END_IGNORE_CHAR = '\\';
  private final List<VariableEntry> variableEntries = new ArrayList<>();
  private final List<StringReplacer> externalReplacers = new ArrayList<>();
  private BooleanSupplier replaceAll = () -> false;

  /**
   * Register new variable
   *
   * @param prefix   the prefix
   * @param variable the replacer
   * @param isWhole  whether the manager should check the whole string matches the prefix, set it to false if you want to check if the prefix is at the beginning of the string
   */
  public void register(String prefix, StringReplacer variable, boolean isWhole) {
    variableEntries.add(new VariableEntry(prefix, isWhole, variable));
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
  public List<VariableEntry> getVariableEntries() {
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
   * Get the status whether the manager should replace all similar variables at once
   *
   * @return true if it should
   *
   * @see #setSingleVariables(String, UUID)
   */
  public boolean getReplaceAll() {
    return replaceAll.getAsBoolean();
  }

  /**
   * Whether the manager should replace all similar variables at once
   *
   * @param replaceAll true if it should
   *
   * @see #setSingleVariables(String, UUID)
   */
  public void setReplaceAll(boolean replaceAll) {
    setReplaceAll(() -> replaceAll);
  }

  /**
   * Whether the manager should replace all similar variables at once
   *
   * @param replaceAll the boolean supplier (true if it should)
   *
   * @see #setSingleVariables(String, UUID)
   */
  public void setReplaceAll(BooleanSupplier replaceAll) {
    this.replaceAll = replaceAll;
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
      Optional<String> optionalReplaced = variableEntries.stream()
        .filter(entry -> entry.isWhole ? identifier.equalsIgnoreCase(entry.prefix) : identifier.toLowerCase(Locale.ROOT).startsWith(entry.prefix.toLowerCase(Locale.ROOT)))
        .findFirst()
        .map(entry -> entry.replacer.tryReplace(identifier.substring(entry.prefix.length()), uuid));
      if (optionalReplaced.isPresent()) {
        if (this.getReplaceAll()) {
          message = message.replaceAll(Pattern.quote(original), Matcher.quoteReplacement(optionalReplaced.get()));
        } else {
          message = message.replaceFirst(Pattern.quote(original), Matcher.quoteReplacement(optionalReplaced.get()));
        }
      }
    }

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

  /**
   * A variable entry
   */
  public static class VariableEntry {
    /**
     * The prefix
     */
    public final String prefix;
    /**
     * Whether the manager should check if the whole string matches the prefix, or just the start
     */
    public final boolean isWhole;
    /**
     * The string replacer
     */
    public final StringReplacer replacer;

    private VariableEntry(String prefix, boolean isWhole, StringReplacer replacer) {
      this.prefix = prefix;
      this.isWhole = isWhole;
      this.replacer = replacer;
    }
  }
}
