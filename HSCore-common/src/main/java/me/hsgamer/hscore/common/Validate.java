package me.hsgamer.hscore.common;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public final class Validate {

  private Validate() {

  }

  /**
   * Convert to number
   *
   * @param input the string
   * @return the number
   */
  public static Optional<BigDecimal> getNumber(String input) {
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
   * @return whether it's positive
   */
  public static boolean isValidPositiveNumber(String input) {
    Optional<BigDecimal> number = getNumber(input);
    return number.filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0).isPresent();
  }

  /**
   * Check if the string is a valid number
   *
   * @param input the string
   * @return whether it's valid
   */
  public static boolean isValidInteger(String input) {
    return getNumber(input).isPresent();
  }

  /**
   * Check if the class is loaded
   *
   * @param className the class path
   * @return whether it's loaded
   */
  public static boolean isClassLoaded(String className) {
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
   * @return whether it's loaded
   */
  public static boolean isMethodLoaded(String className, String methodName, Class<?>... params) {
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
   * @return whether it's loaded
   */
  public static boolean isConstructorLoaded(String className, Class<?>... params) {
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
   * @return whether it's null or empty
   */
  public static boolean isNullOrEmpty(Collection<?> list) {
    return list == null || list.isEmpty();
  }

  /**
   * Check if it's null or empty
   *
   * @param string the string
   * @return whether it's null or empty
   */
  public static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }
}