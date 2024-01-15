package me.hsgamer.hscore.license.spigotmc;

/**
 * Fetch the {@link SpigotLicenseEntry}
 */
public interface SpigotLicenseFetcher {
  /**
   * The default fetcher.
   * The values will be replaced by SpigotMC when uploading the resource.
   *
   * @return the default fetcher
   */
  static SpigotLicenseFetcher defaultFetcher() {
    return () -> {
      // Will be replaced by SpigotMC
      return new SpigotLicenseEntry(
        "%%__USER__%%",
        "%%__RESOURCE__%%",
        "%%__NONCE__%%"
      );
    };
  }

  /**
   * Fetch the license entry
   *
   * @return the license entry
   */
  SpigotLicenseEntry fetchLicense();
}
