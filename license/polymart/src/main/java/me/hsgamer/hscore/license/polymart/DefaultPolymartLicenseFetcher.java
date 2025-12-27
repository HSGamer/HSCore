package me.hsgamer.hscore.license.polymart;

import java.util.Objects;

/**
 * The default {@link PolymartLicenseFetcher}
 */
public class DefaultPolymartLicenseFetcher implements PolymartLicenseFetcher {
  public static String getUser() {
    return "%%__USER__%%";
  }

  public static String getUsername() {
    return "%%__USERNAME__%%";
  }

  public static String getResource() {
    return "%%__RESOURCE__%%";
  }

  public static String getResourceVersion() {
    return "%%__RESOURCE_VERSION__%%";
  }

  public static String getNonce() {
    return "%%__NONCE__%%";
  }

  public static String getVerifyToken() {
    return "%%__VERIFY_TOKEN__%%";
  }

  public static String getLicense() {
    return "%%__LICENSE__%%";
  }

  public static String getAgent() {
    return "%%__AGENT__%%";
  }

  public static String getTimestamp() {
    return "%%__TIMESTAMP__%%";
  }

  public static String getAvailableIdentifier() {
    return "%%__POLYMART__%%";
  }

  @Override
  public boolean isAvailable() {
    return Objects.equals(getAvailableIdentifier(), "1");
  }

  @Override
  public PolymartLicenseEntry fetchLicense() {
    return new PolymartLicenseEntry(
      getUser(),
      getUsername(),
      getResource(),
      getResourceVersion(),
      getNonce(),
      getVerifyToken(),
      getLicense(),
      getAgent(),
      getTimestamp()
    );
  }
}
