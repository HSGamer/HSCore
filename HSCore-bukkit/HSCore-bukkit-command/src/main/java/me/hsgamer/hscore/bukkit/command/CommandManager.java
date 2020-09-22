package me.hsgamer.hscore.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * The command manager
 */
public class CommandManager {

  protected final Map<String, Command> registered = new HashMap<>();
  protected final JavaPlugin plugin;
  protected final Field knownCommandsField;
  protected final CommandMap bukkitCommandMap;
  protected Method syncCommandsMethod;

  /**
   * Create a new command manager
   *
   * @param plugin the plugin
   */
  public CommandManager(@NotNull final JavaPlugin plugin) {
    this.plugin = plugin;
    try {
      Method commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap");
      this.bukkitCommandMap = (CommandMap) commandMapMethod.invoke(Bukkit.getServer());

      this.knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
      this.knownCommandsField.setAccessible(true);
    } catch (ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }

    try {
      Class<?> craftServer = Bukkit.getServer().getClass();
      this.syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
    } catch (Exception e) {
      // Ignored
    }
    if (this.syncCommandsMethod != null) {
      this.syncCommandsMethod.setAccessible(true);
    }
  }

  /**
   * Register the command
   *
   * @param command the command object
   */
  public final void register(@NotNull final Command command) {
    String name = command.getLabel();
    if (this.registered.containsKey(name)) {
      this.plugin.getLogger().log(Level.WARNING, "Duplicated \"{0}\" command ! Ignored", name);
      return;
    }

    this.bukkitCommandMap.register(this.plugin.getName(), command);
    this.registered.put(name, command);
  }

  /**
   * Unregister the command
   *
   * @param command the command object
   */
  public final void unregister(@NotNull final Command command) {
    try {
      Map<?, ?> knownCommands = (Map<?, ?>) this.knownCommandsField.get(this.bukkitCommandMap);

      knownCommands.values().removeIf(command::equals);

      command.unregister(this.bukkitCommandMap);
      this.registered.remove(command.getLabel());
    } catch (ReflectiveOperationException e) {
      this.plugin.getLogger()
        .log(Level.WARNING, "Something wrong when unregister the command", e);
    }
  }

  /**
   * Unregister the command
   *
   * @param command the command label
   */
  public final void unregister(@NotNull final String command) {
    if (this.registered.containsKey(command)) {
      unregister(this.registered.remove(command));
    }
  }

  /**
   * Sync the commands to the server. Mainly used to make tab completer work in 1.13+
   */
  public final void syncCommand() {
    if (this.syncCommandsMethod == null) {
      return;
    }

    try {
      this.syncCommandsMethod.invoke(this.plugin.getServer());
    } catch (IllegalAccessException | InvocationTargetException e) {
      this.plugin.getLogger().log(Level.WARNING, "Error when syncing commands", e);
    }
  }

  /**
   * Get registered commands
   *
   * @return the map contains the name and the command object
   */
  @NotNull
  public final Map<String, Command> getRegistered() {
    return this.registered;
  }
}
