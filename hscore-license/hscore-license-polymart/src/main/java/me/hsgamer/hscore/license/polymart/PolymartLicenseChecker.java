package me.hsgamer.hscore.license.polymart;

import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.common.LicenseStatus;

// TODO: Use Polymart API to check the license
public class PolymartLicenseChecker implements LicenseChecker {
  private final String resource;
  private final boolean isPaid;
  private final PolymartLicenseFetcher fetcher;

  public PolymartLicenseChecker(String resource, boolean isPaid, PolymartLicenseFetcher fetcher) {
    this.resource = resource;
    this.isPaid = isPaid;
    this.fetcher = fetcher;
  }

  public PolymartLicenseChecker(String resource, boolean isPaid) {
    this(resource, isPaid, PolymartLicenseFetcher.defaultFetcher());
  }

  @Override
  public LicenseResult checkLicense() {
    PolymartLicenseEntry entry = fetcher.fetchLicense();

    LicenseStatus status;
    if (!entry.isValid(isPaid)) {
      status = LicenseStatus.INVALID;
    } else if (resource.equals(entry.resource)) {
      status = LicenseStatus.VALID;
    } else {
      status = LicenseStatus.UNKNOWN;
    }

    return new LicenseResult(status, entry.toProperties());
  }
}
