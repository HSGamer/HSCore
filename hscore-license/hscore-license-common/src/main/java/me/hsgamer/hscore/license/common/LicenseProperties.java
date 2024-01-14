package me.hsgamer.hscore.license.common;

import java.util.Properties;

public class LicenseProperties extends Properties {
  public LicenseProperties() {
    super();
  }

  public LicenseProperties(Properties defaults) {
    super(defaults);
  }

  public String getProperty(CommonLicenseProperty key) {
    return getProperty(key.getKey());
  }

  public void setProperty(CommonLicenseProperty key, String value) {
    setProperty(key.getKey(), value);
  }
}
