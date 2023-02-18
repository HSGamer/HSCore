package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.addon.object.Addon;
import me.hsgamer.hscore.expansion.common.ExpansionManager;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Addon class for Bukkit
 */
public abstract class PluginAddon extends Addon {
  private Plugin fakePlugin;

  /**
   * Get the plugin that the addon is currently on
   *
   * @return the plugin
   */
  public final JavaPlugin getPlugin() {
    final ExpansionManager addonManager = this.getExpansionClassLoader().getManager();
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
    return this.getPlugin().getServer();
  }

  /**
   * Check if the addon has a fake plugin
   *
   * @return true if the addon has a fake plugin
   */
  protected boolean hasFakePlugin() {
    return false;
  }

  /**
   * Load the fake plugin
   *
   * @throws InvalidPluginException      if the plugin is invalid
   * @throws InvalidDescriptionException if the description is invalid
   */
  public final void loadFakePlugin() throws InvalidPluginException, InvalidDescriptionException {
    if (hasFakePlugin()) {
      this.fakePlugin = this.getPlugin().getServer().getPluginManager().loadPlugin(this.getExpansionClassLoader().getFile());
    }
  }

  /**
   * Enable the fake plugin
   */
  public final void enableFakePlugin() {
    if (hasFakePlugin() && this.fakePlugin != null) {
      this.getPlugin().getServer().getPluginManager().enablePlugin(this.fakePlugin);
    }
  }

  /**
   * Disable the fake plugin
   */
  public final void disableFakePlugin() {
    if (hasFakePlugin() && this.fakePlugin != null) {
      this.getPlugin().getServer().getPluginManager().disablePlugin(this.fakePlugin);
    }
  }

  /**
   * Get the fake plugin
   *
   * @return the fake plugin
   */
  public final Plugin getFakePlugin() {
    return fakePlugin;
  }
}
