package me.hsgamer.hscore.license.template;

import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.common.LicenseStatus;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A template to fetch messages based on the {@link LicenseStatus}
 */
public class LicenseTemplate {
  private static final String URL_VARIABLE = "{url}";
  private final List<LicenseChecker> checkers;
  private final Map<LicenseStatus, Function<List<LicenseResult>, List<String>>> messages = new EnumMap<>(LicenseStatus.class);

  /**
   * Create a new template
   *
   * @param checkers the license checkers
   */
  public LicenseTemplate(List<LicenseChecker> checkers) {
    this.checkers = checkers;
  }

  /**
   * Create a new template
   *
   * @param checker the license checker
   */
  public LicenseTemplate(LicenseChecker... checker) {
    this(Arrays.asList(checker));
  }

  /**
   * Add the message
   *
   * @param status  the status
   * @param message the message
   */
  public void addMessage(LicenseStatus status, Function<List<LicenseResult>, List<String>> message) {
    messages.put(status, message);
  }

  /**
   * Add the message
   *
   * @param status  the status
   * @param message the message
   */
  public void addMessage(LicenseStatus status, List<String> message) {
    messages.put(status, r -> message);
  }

  /**
   * Add the message
   *
   * @param status  the status
   * @param message the message
   */
  public void addMessage(LicenseStatus status, String... message) {
    List<String> licenseMessageList = Arrays.asList(message);
    messages.put(status, r -> licenseMessageList);
  }

  /**
   * Add the default message
   *
   * @param name the name of the product
   */
  public void addDefaultMessage(String name) {
    addMessage(
      LicenseStatus.VALID,
      "Thank you for supporting " + name + ". Your support is greatly appreciated"
    );
    addMessage(
      LicenseStatus.INVALID,
      "Thank you for using " + name,
      "If you like this product, please consider supporting it by purchasing from one of these platforms:",
      "- " + URL_VARIABLE
    );
    addMessage(
      LicenseStatus.OFFLINE,
      "Cannot check your license for " + name + ". Please check your internet connection",
      "Note: You can still use this product without a license, and there is no limit on the features",
      "If you like this product, please consider supporting it by purchasing from one of these platforms:",
      "- " + URL_VARIABLE
    );
    addMessage(
      LicenseStatus.UNKNOWN,
      "Cannot check your license for " + name + ". Please try again later",
      "Note: You can still use this product without a license, and there is no limit on the features",
      "If you like this product, please consider supporting it by purchasing from one of these platforms:",
      "- " + URL_VARIABLE
    );
  }

  /**
   * Check the license and get the message
   *
   * @return the message
   */
  public List<String> getMessage() {
    List<LicenseResult> licenseResults = checkers.stream()
      .filter(LicenseChecker::isAvailable)
      .map(LicenseChecker::checkLicense)
      .collect(Collectors.toList());

    List<String> messages = Collections.emptyList();
    for (LicenseStatus status : LicenseStatus.values()) {
      if (licenseResults.stream().anyMatch(l -> l.getStatus().equals(status))) {
        Function<List<LicenseResult>, List<String>> function = this.messages.get(status);
        if (function != null) {
          messages = function.apply(licenseResults);
        }
        break;
      }
    }

    return messages.stream()
      .flatMap(s -> {
        if (s.contains(URL_VARIABLE)) {
          return checkers.stream()
            .map(checker -> s.replace(Pattern.quote(URL_VARIABLE), checker.getUrl()));
        } else {
          return Stream.of(s);
        }
      })
      .collect(Collectors.toList());
  }

  /**
   * Check the license and get the message
   *
   * @param transform the function to transform the message
   *
   * @return the message
   */
  public List<String> getMessage(UnaryOperator<String> transform) {
    return getMessage().stream().map(transform).collect(Collectors.toList());
  }
}
