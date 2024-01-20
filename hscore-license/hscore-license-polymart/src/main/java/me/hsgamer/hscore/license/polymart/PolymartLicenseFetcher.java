package me.hsgamer.hscore.license.polymart;

/**
 * Fetch the {@link PolymartLicenseEntry}
 */
public interface PolymartLicenseFetcher {
  /**
   * The default fetcher.
   * The values will be replaced by Polymart when uploading the resource.
   *
   * @return the default fetcher
   */
  static PolymartLicenseFetcher defaultFetcher() {
    // Will be replaced by Polymart
    String string = "%%__USER__%%||%%__USERNAME__%%||%%__RESOURCE__%%||%%__RESOURCE_VERSION__%%||%%__NONCE__%%||%%__VERIFY_TOKEN__%%||%%__LICENSE__%%||%%__AGENT__%%||%%__TIMESTAMP__%%";
    String[] split = string.split("\\|\\|");
    return () -> new PolymartLicenseEntry(
      split[0],
      split[1],
      split[2],
      split[3],
      split[4],
      split[5],
      split[6],
      split[7],
      split[8]
    );
  }

  /**
   * Fetch the license entry
   *
   * @return the license entry
   */
  PolymartLicenseEntry fetchLicense();
}
