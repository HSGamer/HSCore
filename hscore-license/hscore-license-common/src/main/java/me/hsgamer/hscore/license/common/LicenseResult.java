package me.hsgamer.hscore.license.common;

/**
 * The result from {@link LicenseChecker}
 */
public class LicenseResult {
  private final LicenseStatus status;
  private final LicenseProperties properties;

  /**
   * Create a new result
   *
   * @param status     the status
   * @param properties the properties
   */
  public LicenseResult(LicenseStatus status, LicenseProperties properties) {
    this.status = status;
    this.properties = properties;
  }

  /**
   * Get the status
   *
   * @return the status
   */
  public LicenseStatus getStatus() {
    return status;
  }

  /**
   * Get the properties
   *
   * @return the properties
   */
  public LicenseProperties getProperties() {
    return properties;
  }
}
