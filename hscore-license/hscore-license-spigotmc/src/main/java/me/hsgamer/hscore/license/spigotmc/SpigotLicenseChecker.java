package me.hsgamer.hscore.license.spigotmc;

import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;

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
    return new LicenseResult(entry.isValid() && resource.equals(entry.getResource()), entry.toProperties());
  }
}
