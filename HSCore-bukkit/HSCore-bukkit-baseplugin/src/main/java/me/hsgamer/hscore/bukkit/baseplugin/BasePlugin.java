package me.hsgamer.hscore.bukkit.baseplugin;

import me.hsgamer.hscore.bukkit.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * A slightly complicated JavaPlugin implementation
 */
public abstract class BasePlugin extends JavaPlugin {
  private final CommandManager commandManager = new CommandManager(this);

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

    getServer().getScheduler().cancelTasks(this);
    HandlerList.unregisterAll(this);
    commandManager.unregisterAll();
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
   * Register the command
   *
   * @param command the command
   */
  public void registerCommand(Command command) {
    commandManager.register(command);
  }

  /**
   * Register the listener
   *
   * @param listener the listener
   */
  public void registerListener(Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }

  /**
   * Get a registered command
   *
   * @param label the label
   *
   * @return the command
   */
  public Command getRegisteredCommand(String label) {
    Command command = commandManager.getRegistered().get(label);
    return command != null ? command : getCommand(label);
  }

  /**
   * Get the command manager
   *
   * @return the command manager
   */
  public CommandManager getCommandManager() {
    return commandManager;
  }
}
