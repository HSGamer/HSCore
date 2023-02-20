package me.hsgamer.hscore.addon;

import me.hsgamer.hscore.addon.object.Addon;
import me.hsgamer.hscore.expansion.common.ExpansionDescription;
import me.hsgamer.hscore.expansion.common.ExpansionManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages all addons in it
 */
public class AddonManager extends ExpansionManager {
  private final Logger logger;

  /**
   * Create a new addon manager
   *
   * @param addonsDir              the directory to store addon files
   * @param logger                 the logger to use in every addon
   * @param addonDescriptionLoader the loader to load addon description
   * @param parentClassLoader      the parent class loader to load all addons
   */
  public AddonManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> addonDescriptionLoader, @NotNull final ClassLoader parentClassLoader) {
    super(addonsDir, addonDescriptionLoader, parentClassLoader);
    this.logger = logger;
    addStateListener((loader, state) -> {
      Addon addon = loader.getExpansionOptional().filter(Addon.class::isInstance).map(Addon.class::cast).orElse(null);
      if (addon == null) return;
      switch (state) {
        case LOADING:
          if (!onAddonLoading(addon)) {
            throw new IllegalStateException("Cannot load " + loader.getDescription().getName());
          }
          break;
        case ENABLING:
          onAddonEnable(addon);
          break;
        case ENABLED:
          onAddonEnabled(addon);
          break;
        case DISABLING:
          onAddonDisable(addon);
          break;
        case DISABLED:
          onAddonDisabled(addon);
          break;
        default:
          break;
      }
    });
    addStateListener((loader, state) -> {
      switch (state) {
        case ERROR:
          logger.log(Level.WARNING, loader.getThrowable(), () -> "There is an error in " + loader.getDescription().getName());
          break;
        case LOADED:
          logger.info(() -> "Loaded " + loader.getDescription().getName() + " " + loader.getDescription().getVersion());
          break;
        case ENABLED:
          logger.info(() -> "Enabled " + loader.getDescription().getName() + " " + loader.getDescription().getVersion());
          break;
        case DISABLED:
          logger.info(() -> "Disabled " + loader.getDescription().getName() + " " + loader.getDescription().getVersion());
          break;
        default:
          break;
      }
    });
    setExceptionHandler(t -> logger.log(Level.WARNING, "There is an unexpected exception on AddonManager", t));
  }

  /**
   * Create a new addon manager
   *
   * @param addonsDir              the directory to store addon files
   * @param logger                 the logger to use in every addon
   * @param addonDescriptionLoader the loader to load addon description
   */
  public AddonManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> addonDescriptionLoader) {
    this(addonsDir, logger, addonDescriptionLoader, AddonManager.class.getClassLoader());
  }

  /**
   * Call the {@link Addon#onPostEnable()} method of all enabled addons
   */
  public void callPostEnable() {
    call(Addon.class, Addon::onPostEnable);
  }

  /**
   * Call the {@link Addon#onReload()} method of all enabled addons
   */
  public void callReload() {
    call(Addon.class, Addon::onReload);
  }

  /**
   * Called when the addon is on loading
   *
   * @param addon the loading addon
   *
   * @return whether the addon is properly loaded
   */
  protected boolean onAddonLoading(@NotNull final Addon addon) {
    return true;
  }

  /**
   * Called when the addon is on enable
   *
   * @param addon the enabling addon
   */
  protected void onAddonEnable(@NotNull final Addon addon) {
    // EMPTY
  }

  /**
   * Called when the addon is enabled
   *
   * @param addon the enabled addon
   */
  protected void onAddonEnabled(@NotNull final Addon addon) {
    // EMPTY
  }

  /**
   * Called when the addon is on disabling
   *
   * @param addon the disabling addon
   */
  protected void onAddonDisable(@NotNull final Addon addon) {
    // EMPTY
  }

  /**
   * Called when the addon is disabled
   *
   * @param addon the disabled addon
   */
  protected void onAddonDisabled(@NotNull final Addon addon) {
    // EMPTY
  }

  /**
   * Get the logger
   *
   * @return the logger
   */
  public Logger getLogger() {
    return logger;
  }
}
