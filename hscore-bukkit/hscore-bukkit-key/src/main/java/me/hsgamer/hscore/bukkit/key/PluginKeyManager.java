package me.hsgamer.hscore.bukkit.key;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/**
 * A {@link KeyManager} for the {@link Plugin}
 */
public class PluginKeyManager extends AbstractKeyManager {
  private final Plugin plugin;

  /**
   * Create a new {@link NamespacedKey} manager
   *
   * @param plugin the holder plugin
   */
  public PluginKeyManager(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public NamespacedKey newKey(String key) {
    return new NamespacedKey(plugin, key);
  }
}
