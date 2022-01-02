package me.hsgamer.hscore.bukkit.baseplugin;

import me.hsgamer.hscore.bukkit.command.CommandManager;
import me.hsgamer.hscore.bukkit.simpleplugin.SimplePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;

/**
 * A slightly complicated {@link SimplePlugin} implementation
 */
public abstract class BasePlugin extends SimplePlugin {
  private final CommandManager commandManager = new CommandManager(this);

  @Override
  public final void onLoad() {
    super.onLoad();
  }

  @Override
  public final void onEnable() {
    super.onEnable();
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, CommandManager::syncCommand);
  }

  @Override
  public final void onDisable() {
    super.onDisable();
    commandManager.unregisterAll();
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
