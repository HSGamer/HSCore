package me.hsgamer.hscore.license.spigotmc;

public interface SpigotLicenseFetcher {
  static SpigotLicenseFetcher defaultFetcher() {
    return () -> {
      // Will be replaced by SpigotMC
      return new SpigotLicenseEntry(
        "%%__USER__%%",
        "%%__RESOURCE__%%",
        "%%__NONCE__%%"
      );
    };
  }

  SpigotLicenseEntry fetchLicense();
}
