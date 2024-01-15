package me.hsgamer.hscore.license.common;

import java.util.Properties;

/**
 * The license properties
 */
public class LicenseProperties extends Properties {
  /**
   * Create a new instance
   */
  public LicenseProperties() {
    super();
  }

  /**
   * Create a new instance
   *
   * @param defaults the default properties
   */
  public LicenseProperties(Properties defaults) {
    super(defaults);
  }

  /**
   * Get the property
   *
   * @param key the key
   *
   * @return the property
   */
  public String getProperty(CommonLicenseProperty key) {
    return getProperty(key.getKey());
  }

  /**
   * Set the property
   *
   * @param key   the key
   * @param value the value
   */
  public void setProperty(CommonLicenseProperty key, String value) {
    setProperty(key.getKey(), value);
  }
}
