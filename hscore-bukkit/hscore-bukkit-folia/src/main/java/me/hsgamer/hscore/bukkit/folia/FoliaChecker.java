package me.hsgamer.hscore.bukkit.folia;

/**
 * The checker for Folia
 */
public final class FoliaChecker {
  private static final boolean IS_FOLIA;

  static {
    boolean isFolia;
    try {
      Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
      isFolia = true;
    } catch (ClassNotFoundException ignored) {
      isFolia = false;
    }
    IS_FOLIA = isFolia;
  }

  private FoliaChecker() {
    // EMPTY
  }

  /**
   * Check if the server is running Folia
   *
   * @return true if the server is running Folia
   */
  public static boolean isFolia() {
    return IS_FOLIA;
  }
}
