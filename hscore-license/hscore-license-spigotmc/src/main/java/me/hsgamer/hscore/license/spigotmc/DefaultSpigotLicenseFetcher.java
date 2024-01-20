package me.hsgamer.hscore.license.spigotmc;

/**
 * The default {@link SpigotLicenseFetcher}
 */
public class DefaultSpigotLicenseFetcher implements SpigotLicenseFetcher {
  public static String getUser() {
    return "%%__USER__%%";
  }

  public static String getResource() {
    return "%%__RESOURCE__%%";
  }

  public static String getNonce() {
    return "%%__NONCE__%%";
  }

  @Override
  public SpigotLicenseEntry fetchLicense() {
    return new SpigotLicenseEntry(getUser(), getResource(), getNonce());
  }
}
