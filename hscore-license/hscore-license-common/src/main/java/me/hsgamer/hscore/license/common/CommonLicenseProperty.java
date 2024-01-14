package me.hsgamer.hscore.license.common;

public enum CommonLicenseProperty {
  USER("user"),
  RESOURCE("resource"),
  NONCE("nonce"),
  TYPE("type"),
  ;
  private final String key;

  CommonLicenseProperty(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
