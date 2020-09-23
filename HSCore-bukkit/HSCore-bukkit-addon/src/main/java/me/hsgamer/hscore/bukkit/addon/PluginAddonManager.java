package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.addon.AddonManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Addon manager for Bukkit
 */
public abstract class PluginAddonManager extends AddonManager {

  private final JavaPlugin javaPlugin;

  /**
   * Create a new addon manager
   *
   * @param javaPlugin the plugin
   */
  public PluginAddonManager(@NotNull final JavaPlugin javaPlugin) {
    super(new File(javaPlugin.getDataFolder(), "addon"), javaPlugin.getLogger());
    this.javaPlugin = javaPlugin;
  }

  /**
   * Get the plugin
   *
   * @return the plugin
   */
  public final JavaPlugin getPlugin() {
    return javaPlugin;
  }
}
