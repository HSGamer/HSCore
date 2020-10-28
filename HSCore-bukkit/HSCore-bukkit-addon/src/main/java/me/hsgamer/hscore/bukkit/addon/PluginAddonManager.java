package me.hsgamer.hscore.bukkit.addon;

import java.io.File;
import me.hsgamer.hscore.addon.AddonManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
   * @param javaPlugin the plugin
   */
  protected PluginAddonManager(@NotNull final JavaPlugin javaPlugin) {
    super(new File(javaPlugin.getDataFolder(), "addon"), javaPlugin.getLogger());
    this.javaPlugin = javaPlugin;
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
}
