package me.hsgamer.hscore.license.common;

public class LicenseResult {
  private final boolean valid;
  private final LicenseProperties properties;

  public LicenseResult(boolean valid, LicenseProperties properties) {
    this.valid = valid;
    this.properties = properties;
  }

  public boolean isValid() {
    return valid;
  }

  public LicenseProperties getProperties() {
    return properties;
  }
}
