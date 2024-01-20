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
    return new DefaultPolymartLicenseFetcher();
  }

  /**
   * Fetch the license entry
   *
   * @return the license entry
   */
  PolymartLicenseEntry fetchLicense();
}
