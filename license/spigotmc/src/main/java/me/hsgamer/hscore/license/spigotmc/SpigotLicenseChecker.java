package me.hsgamer.hscore.license.spigotmc;

import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.common.LicenseStatus;

/**
 * The license checker for SpigotMC
 */
public class SpigotLicenseChecker implements LicenseChecker {
  private final String resource;
  private final SpigotLicenseFetcher fetcher;

  /**
   * Create a new license checker
   *
   * @param resource the resource id
   * @param fetcher  the fetcher
   */
  public SpigotLicenseChecker(String resource, SpigotLicenseFetcher fetcher) {
    this.resource = resource;
    this.fetcher = fetcher;
  }

  /**
   * Create a new license checker with the default fetcher
   *
   * @param resource the resource id
   */
  public SpigotLicenseChecker(String resource) {
    this(resource, SpigotLicenseFetcher.defaultFetcher());
  }

  @Override
  public LicenseResult checkLicense() {
    SpigotLicenseEntry entry = fetcher.fetchLicense();

    LicenseStatus status;
    if (!entry.isValid()) {
      status = LicenseStatus.INVALID;
    } else if (resource.equals(entry.resource)) {
      status = LicenseStatus.VALID;
    } else {
      status = LicenseStatus.UNKNOWN;
    }

    return new LicenseResult(status, entry.toProperties());
  }

  @Override
  public String getUrl() {
    return "https://www.spigotmc.org/resources/" + resource;
  }
}
