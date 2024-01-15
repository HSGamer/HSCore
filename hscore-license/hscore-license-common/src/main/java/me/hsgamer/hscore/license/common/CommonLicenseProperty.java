package me.hsgamer.hscore.license.common;

/**
 * The common license properties for {@link LicenseProperties}
 */
public enum CommonLicenseProperty {
  /**
   * The identifier of the user
   */
  USER("user"),
  /**
   * The identifier of the resource
   */
  RESOURCE("resource"),
  /**
   * The unique identifier of the license
   */
  NONCE("nonce"),
  /**
   * The type of the license
   */
  TYPE("type"),
  ;
  private final String key;

  CommonLicenseProperty(String key) {
    this.key = key;
  }

  /**
   * Get the key
   *
   * @return the key
   */
  public String getKey() {
    return key;
  }
}
