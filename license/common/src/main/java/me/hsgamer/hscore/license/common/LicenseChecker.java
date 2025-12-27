package me.hsgamer.hscore.license.common;

/**
 * The license checker
 */
public interface LicenseChecker {
  /**
   * Check the license
   *
   * @return the result
   */
  LicenseResult checkLicense();

  /**
   * Get the URL of the platform this license refers to
   *
   * @return the URL
   */
  String getUrl();
}
