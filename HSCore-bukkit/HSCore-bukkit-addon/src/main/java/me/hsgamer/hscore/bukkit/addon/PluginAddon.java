package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.addon.AddonManager;
import me.hsgamer.hscore.addon.object.Addon;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Addon class for Bukkit
 */
public abstract class PluginAddon extends Addon {

  /**
   * Get the plugin that the addon is currently on
   *
   * @return the plugin
   */
  public final JavaPlugin getPlugin() {
    AddonManager addonManager = getAddonManager();
    if (!(addonManager instanceof PluginAddonManager)) {
      throw new IllegalStateException("You are calling the method on a non-Bukkit project");
    }
    return ((PluginAddonManager) addonManager).getPlugin();
  }

  /**
   * Get the server that the addon is currently on
   *
   * @return the server
   */
  public final Server getServer() {
    return getPlugin().getServer();
  }
}
