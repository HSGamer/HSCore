package me.hsgamer.hscore.license.spigotmc;

import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.common.LicenseStatus;

public class SpigotLicenseChecker implements LicenseChecker {
  private final String resource;
  private final SpigotLicenseFetcher fetcher;

  public SpigotLicenseChecker(String resource, SpigotLicenseFetcher fetcher) {
    this.resource = resource;
    this.fetcher = fetcher;
  }

  public SpigotLicenseChecker(String resource) {
    this(resource, SpigotLicenseFetcher.defaultFetcher());
  }

  @Override
  public LicenseResult checkLicense() {
    SpigotLicenseEntry entry = fetcher.fetchLicense();

    LicenseStatus status;
    if (!entry.isValid()) {
      status = LicenseStatus.INVALID;
    } else if (entry.getResource().equals(resource)) {
      status = LicenseStatus.VALID;
    } else {
      status = LicenseStatus.UNKNOWN;
    }

    return new LicenseResult(status, entry.toProperties());
  }
}
