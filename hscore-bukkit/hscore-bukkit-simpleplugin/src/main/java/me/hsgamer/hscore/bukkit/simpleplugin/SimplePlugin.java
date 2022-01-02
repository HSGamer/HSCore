package me.hsgamer.hscore.bukkit.simpleplugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * A convenient {@link JavaPlugin} implementation
 */
public abstract class SimplePlugin extends JavaPlugin {
  public SimplePlugin() {
    super();
    preLoad();
  }

  protected SimplePlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
    super(loader, description, dataFolder, file);
    preLoad();
  }

  @Override
  public void onLoad() {
    load();
  }

  @Override
  public void onEnable() {
    enable();

    Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::postEnable);
  }

  @Override
  public void onDisable() {
    disable();

    getServer().getScheduler().cancelTasks(this);
    getServer().getServicesManager().unregisterAll(this);
    HandlerList.unregisterAll(this);

    postDisable();
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

  /**
   * Execute after disabling all tasks, listeners and commands
   */
  public void postDisable() {
    // Post Disable logic
  }

  /**
   * Register the listener
   *
   * @param listener the listener
   */
  public void registerListener(Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }
}
