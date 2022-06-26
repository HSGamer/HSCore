package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.addon.AddonManager;
import me.hsgamer.hscore.addon.object.Addon;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

/**
 * Addon manager for Bukkit
 */
public abstract class PluginAddonManager extends AddonManager {

  /**
   * The addon's plugin instance
   */
  @NotNull
  private final JavaPlugin javaPlugin;

  /**
   * Create a new addon manager
   *
   * @param javaPlugin  the plugin
   * @param addonFolder the addon folder
   */
  protected PluginAddonManager(@NotNull final JavaPlugin javaPlugin, @NotNull final File addonFolder) {
    super(addonFolder, javaPlugin.getLogger());
    this.javaPlugin = javaPlugin;
  }

  /**
   * Create a new addon manager
   *
   * @param javaPlugin the plugin
   */
  protected PluginAddonManager(@NotNull final JavaPlugin javaPlugin) {
    this(javaPlugin, new File(javaPlugin.getDataFolder(), "addon"));
  }

  /**
   * Get the plugin
   *
   * @return the plugin
   */
  @NotNull
  public final JavaPlugin getPlugin() {
    return this.javaPlugin;
  }

  @Override
  protected void onAddonEnabled(@NotNull Addon addon) {
    if (addon instanceof PluginAddon) {
      PluginAddon pluginAddon = (PluginAddon) addon;
      try {
        pluginAddon.loadFakePlugin();
        pluginAddon.enableFakePlugin();
      } catch (InvalidPluginException | InvalidDescriptionException e) {
        getLogger().log(Level.WARNING, e, () -> "Failed to load fake plugin for " + addon.getDescription().getName());
      }
    }
  }

  @Override
  protected void onAddonDisabled(@NotNull Addon addon) {
    if (addon instanceof PluginAddon) {
      ((PluginAddon) addon).disableFakePlugin();
    }
  }
}
