package me.hsgamer.hscore.bukkit.baseplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * A slightly complicated JavaPlugin implementation
 */
public abstract class BasePlugin extends JavaPlugin {
  public BasePlugin() {
    super();
    preLoad();
  }

  protected BasePlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
    super(loader, description, dataFolder, file);
    preLoad();
  }

  @Override
  public final void onLoad() {
    load();
  }

  @Override
  public final void onEnable() {
    enable();

    Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::postEnable);
  }

  @Override
  public final void onDisable() {
    disable();
  }

  /**
   * Execute on the constructor
   */
  public void preLoad() {
    // Preload logic
  }

  /**
   * Execute when loading the plugin
   */
  public void load() {
    // Load logic
  }

  /**
   * Execute when enabling the plugin
   */
  public void enable() {
    // Enable logic
  }

  /**
   * Execute after enabling all plugins
   */
  public void postEnable() {
    // Post Enable logic
  }

  /**
   * Execute when disabling the plugin
   */
  public void disable() {
    // Disable logic
  }
}
