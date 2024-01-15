package me.hsgamer.hscore.license.spigotmc;

import me.hsgamer.hscore.license.common.CommonLicenseProperty;
import me.hsgamer.hscore.license.common.LicenseProperties;

/**
 * The license entry for SpigotMC
 */
public class SpigotLicenseEntry {
  public final String user;
  public final String resource;
  public final String nonce;

  public SpigotLicenseEntry(String user, String resource, String nonce) {
    this.user = user;
    this.resource = resource;
    this.nonce = nonce;
  }

  /**
   * Check if the entry is valid
   *
   * @return true if it is
   */
  public boolean isValid() {
    return
      user != null && !user.contains("__USER__")
        && resource != null && !resource.contains("__RESOURCE__")
        && nonce != null && !nonce.contains("__NONCE__");
  }

  /**
   * Convert to properties
   *
   * @return the properties
   */
  public LicenseProperties toProperties() {
    LicenseProperties properties = new LicenseProperties();
    properties.setProperty(CommonLicenseProperty.TYPE, "spigotmc");
    properties.setProperty(CommonLicenseProperty.USER, user);
    properties.setProperty(CommonLicenseProperty.RESOURCE, resource);
    properties.setProperty(CommonLicenseProperty.NONCE, nonce);
    return properties;
  }
}
