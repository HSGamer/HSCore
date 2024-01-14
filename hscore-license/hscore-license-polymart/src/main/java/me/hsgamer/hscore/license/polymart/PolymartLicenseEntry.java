package me.hsgamer.hscore.license.polymart;

import me.hsgamer.hscore.license.common.CommonLicenseProperty;
import me.hsgamer.hscore.license.common.LicenseProperties;

public class PolymartLicenseEntry {
  public final String user;
  public final String username;
  public final String resource;
  public final String resourceVersion;
  public final String nonce;
  public final String verifyToken;
  public final String license;
  public final String agent;
  public final String timestamp;

  public PolymartLicenseEntry(String user, String username, String resource, String resourceVersion, String nonce, String verifyToken, String license, String agent, String timestamp) {
    this.user = user;
    this.username = username;
    this.resource = resource;
    this.resourceVersion = resourceVersion;
    this.nonce = nonce;
    this.verifyToken = verifyToken;
    this.license = license;
    this.agent = agent;
    this.timestamp = timestamp;
  }

  public boolean isValid(boolean isPaid) {
    boolean valid =
      user != null && !user.isEmpty() && !user.contains("__USER__")
        && username != null && !username.isEmpty() && !username.contains("__USERNAME__")
        && resource != null && !resource.isEmpty() && !resource.contains("__RESOURCE__")
        && resourceVersion != null && !resourceVersion.isEmpty() && !resourceVersion.contains("__RESOURCE_VERSION__")
        && timestamp != null && !timestamp.isEmpty() && !timestamp.contains("__TIMESTAMP__");

    if (valid && isPaid) {
      valid = nonce != null && !nonce.isEmpty() && !nonce.contains("__NONCE__")
        && verifyToken != null && !verifyToken.isEmpty() && !verifyToken.contains("__VERIFY_TOKEN__")
        && license != null && !license.isEmpty() && !license.contains("__LICENSE__")
        && agent != null && !agent.isEmpty() && !agent.contains("__AGENT__");
    }

    return valid;
  }

  public LicenseProperties toProperties() {
    LicenseProperties properties = new LicenseProperties();
    properties.put(CommonLicenseProperty.TYPE, "polymart");
    properties.put(CommonLicenseProperty.USER, user);
    properties.put("username", username);
    properties.put(CommonLicenseProperty.RESOURCE, resource);
    properties.put("resourceVersion", resourceVersion);
    properties.put(CommonLicenseProperty.NONCE, nonce);
    properties.put("verifyToken", verifyToken);
    properties.put("license", license);
    properties.put("agent", agent);
    properties.put("timestamp", timestamp);
    return properties;
  }
}
