package me.hsgamer.hscore.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

/**
 * Methods to validate
 */
public final class Validate {

  private Validate() {

  }

  /**
   * Convert to number
   *
   * @param input the string
   *
   * @return the number
   */
  @NotNull
  public static Optional<BigDecimal> getNumber(@NotNull String input) {
    try {
      return Optional.of(new BigDecimal(input));
    } catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }

  /**
   * Check if it's a positive number
   *
   * @param input the string
   *
   * @return whether it's positive
   */
  public static boolean isValidPositiveNumber(@NotNull String input) {
    Optional<BigDecimal> number = getNumber(input);
    return number.filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0).isPresent();
  }

  /**
   * Check if the string is a valid number
   *
   * @param input the string
   *
   * @return whether it's valid
   */
  public static boolean isValidInteger(@NotNull String input) {
    return getNumber(input).isPresent();
  }

  /**
   * Check if the class is loaded
   *
   * @param className the class path
   *
   * @return whether it's loaded
   */
  public static boolean isClassLoaded(@NotNull String className) {
    try {
      Class.forName(className);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Check if the method is loaded
   *
   * @param className  the class path
   * @param methodName the method's name
   * @param params     the type of parameters
   *
   * @return whether it's loaded
   */
  public static boolean isMethodLoaded(@NotNull String className, @NotNull String methodName, @NotNull Class<?>... params) {
    try {
      Class.forName(className).getDeclaredMethod(methodName, params);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Check if the constructor is loaded
   *
   * @param className the class path
   * @param params    the type of parameters
   *
   * @return whether it's loaded
   */
  public static boolean isConstructorLoaded(@NotNull String className, @NotNull Class<?>... params) {
    try {
      Class.forName(className).getDeclaredConstructor(params);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Check if it's null or empty
   *
   * @param list the list
   *
   * @return whether it's null or empty
   */
  public static boolean isNullOrEmpty(@Nullable Collection<?> list) {
    return list == null || list.isEmpty();
  }

  /**
   * Check if it's null or empty
   *
   * @param string the string
   *
   * @return whether it's null or empty
   */
  public static boolean isNullOrEmpty(@Nullable String string) {
    return string == null || string.isEmpty();
  }

  /**
   * Check if the string is a valid URL
   *
   * @param string the input string
   *
   * @return true if it is
   */
  public static boolean isValidURL(String string) {
    try {
      new URL(string).toURI();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}