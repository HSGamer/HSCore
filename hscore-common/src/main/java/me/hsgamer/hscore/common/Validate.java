package me.hsgamer.hscore.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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
  public static Optional<BigDecimal> getNumber(@NotNull String input) {
    try {
      return Optional.of(new BigDecimal(input));
    } catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }

  /**
   * Convert to URL
   *
   * @param input the string
   *
   * @return the URL
   */
  public static Optional<URL> getURL(@NotNull String input) {
    try {
      return Optional.of(new URL(input));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Convert to Base64
   *
   * @param input the input
   *
   * @return the Base64
   */
  public static Optional<byte[]> getBase64(@NotNull String input) {
    try {
      return Optional.of(Base64.getDecoder().decode(input));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Convert to UUID
   *
   * @param input the input
   *
   * @return the UUID
   */
  public static Optional<UUID> getUUID(@NotNull String input) {
    try {
      return Optional.of(UUID.fromString(input));
    } catch (Exception e) {
      return Optional.empty();
    }
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
}