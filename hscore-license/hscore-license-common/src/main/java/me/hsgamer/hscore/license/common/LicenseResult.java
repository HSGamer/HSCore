package me.hsgamer.hscore.license.common;

public class LicenseResult {
  private final LicenseStatus status;
  private final LicenseProperties properties;

  public LicenseResult(LicenseStatus status, LicenseProperties properties) {
    this.status = status;
    this.properties = properties;
  }

  public LicenseStatus getStatus() {
    return status;
  }

  public LicenseProperties getProperties() {
    return properties;
  }
}
