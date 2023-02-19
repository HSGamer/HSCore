package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.extra.expansion.DataFolder;
import me.hsgamer.hscore.expansion.extra.expansion.GetClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Logger;

/**
 * The main class of the addon
 */
public abstract class Addon implements Expansion, DataFolder, GetClassLoader {
  private AddonDescription addonDescription;
  private File dataFolder;
  private Logger logger;

  /**
   * Called after all addons enabled
   */
  public void onPostEnable() {
    // EMPTY
  }

  /**
   * Called when reloading
   */
  public void onReload() {
    // EMPTY
  }

  /**
   * Get the addon's description
   *
   * @return the description
   */
  @NotNull
  public final AddonDescription getDescription() {
    if (addonDescription == null) {
      addonDescription = new AddonDescription(this.getExpansionClassLoader().getDescription());
    }
    return addonDescription;
  }

  /**
   * Get the addon's folder
   *
   * @return the directory for the addon
   */
  @NotNull
  public final File getDataFolder() {
    if (dataFolder == null) {
      dataFolder = DataFolder.super.getDataFolder();
      if (!dataFolder.exists()) {
        dataFolder.mkdirs();
      }
    }
    return dataFolder;
  }

  /**
   * Get the addon's logger
   *
   * @return the logger
   */
  public final Logger getLogger() {
    if (logger == null) {
      logger = Logger.getLogger(getDescription().getName());
    }
    return logger;
  }
}
