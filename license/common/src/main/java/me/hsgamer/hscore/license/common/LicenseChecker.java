package me.hsgamer.hscore.license.common;

/**
 * The license checker
 */
public interface LicenseChecker {
  /**
   * Check if the checker is available
   *
   * @return true if it's available
   */
  default boolean isAvailable() {
    return true;
  }

  /**
   * Check the license
   *
   * @return the result
   */
  LicenseResult checkLicense();

  /**
   * Get the URL of the platform this checker refers to
   *
   * @return the URL
   */
  String getUrl();
}
