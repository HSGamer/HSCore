package me.hsgamer.hscore.license.polymart;

public interface PolymartLicenseFetcher {
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

  PolymartLicenseEntry fetchLicense();
}
