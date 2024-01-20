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
    // Will be replaced by SpigotMC
    String string = "%%__USER__%%||%%__RESOURCE__%%||%%__NONCE__%%";
    String[] split = string.split("\\|\\|");
    return () -> new SpigotLicenseEntry(
      split[0],
      split[1],
      split[2]
    );
  }

  /**
   * Fetch the license entry
   *
   * @return the license entry
   */
  SpigotLicenseEntry fetchLicense();
}
