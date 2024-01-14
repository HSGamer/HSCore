package me.hsgamer.hscore.license.spigotmc;

import me.hsgamer.hscore.license.common.CommonLicenseProperty;
import me.hsgamer.hscore.license.common.LicenseProperties;

public class SpigotLicenseEntry {
  private final String user;
  private final String resource;
  private final String nonce;

  public SpigotLicenseEntry(String user, String resource, String nonce) {
    this.user = user;
    this.resource = resource;
    this.nonce = nonce;
  }

  public String getUser() {
    return user;
  }

  public String getResource() {
    return resource;
  }

  public String getNonce() {
    return nonce;
  }

  public boolean isValid() {
    return
      user != null && !user.contains("__USER__")
        && resource != null && !resource.contains("__RESOURCE__")
        && nonce != null && !nonce.contains("__NONCE__");
  }

  public LicenseProperties toProperties() {
    LicenseProperties properties = new LicenseProperties();
    properties.setProperty(CommonLicenseProperty.USER, user);
    properties.setProperty(CommonLicenseProperty.RESOURCE, resource);
    properties.setProperty(CommonLicenseProperty.NONCE, nonce);
    return properties;
  }
}
