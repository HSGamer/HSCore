package me.hsgamer.hscore.license.polymart;

import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.common.LicenseStatus;

/**
 * The license checker for Polymart
 */
// TODO: Use Polymart API to check the license
public class PolymartLicenseChecker implements LicenseChecker {
  private final String resource;
  private final boolean isPaid;
  private final PolymartLicenseFetcher fetcher;

  /**
   * Create a new license checker
   *
   * @param resource the resource id
   * @param isPaid   whether the resource is paid
   * @param fetcher  the license fetcher
   */
  public PolymartLicenseChecker(String resource, boolean isPaid, PolymartLicenseFetcher fetcher) {
    this.resource = resource;
    this.isPaid = isPaid;
    this.fetcher = fetcher;
  }

  /**
   * Create a new license checker with the default fetcher
   *
   * @param resource the resource id
   * @param isPaid   whether the resource is paid
   */
  public PolymartLicenseChecker(String resource, boolean isPaid) {
    this(resource, isPaid, PolymartLicenseFetcher.defaultFetcher());
  }

  /**
   * Check whether the checker can be used
   *
   * @param identifier the identifier
   *
   * @return true if it can be used
   */
  public static boolean isAvailable(String identifier) {
    return "1".equals(identifier);
  }

  /**
   * Check whether the checker can be used
   *
   * @return true if it can be used
   */
  public static boolean isAvailable() {
    return isAvailable("%%__POLYMART__%%");
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
