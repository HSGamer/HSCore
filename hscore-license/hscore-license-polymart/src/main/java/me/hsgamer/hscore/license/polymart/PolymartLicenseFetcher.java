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
    return () -> {
      // Will be replaced by Polymart
      return new PolymartLicenseEntry(
        "%%__USER__%%",
        "%%__USERNAME__%%",
        "%%__RESOURCE__%%",
        "%%__RESOURCE_VERSION__%%",
        "%%__NONCE__%%",
        "%%__VERIFY_TOKEN__%%",
        "%%__LICENSE__%%",
        "%%__AGENT__%%",
        "%%__TIMESTAMP__%%"
      );
    };
  }

  /**
   * Fetch the license entry
   *
   * @return the license entry
   */
  PolymartLicenseEntry fetchLicense();
}
