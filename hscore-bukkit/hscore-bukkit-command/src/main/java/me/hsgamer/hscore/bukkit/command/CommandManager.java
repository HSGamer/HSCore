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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * The command manager
 */
public class CommandManager {

  private static final Field knownCommandsField;
  private static final CommandMap bukkitCommandMap;
  private static Method syncCommandsMethod;

  static {
    try {
      Method commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap");
      bukkitCommandMap = (CommandMap) commandMapMethod.invoke(Bukkit.getServer());

      knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommandsField.setAccessible(true);
    } catch (ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }

    try {
      Class<?> craftServer = Bukkit.getServer().getClass();
      syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
    } catch (Exception e) {
      // Ignored
    } finally {
      if (syncCommandsMethod != null) {
        syncCommandsMethod.setAccessible(true);
      }
    }
  }

  protected final JavaPlugin plugin;
  private final Map<String, Command> registered = new HashMap<>();

  /**
   * Create a new command manager
   *
   * @param plugin the plugin
   */
  public CommandManager(@NotNull final JavaPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Sync the commands to the server. Mainly used to make tab completer work in 1.13+
   */
  public static void syncCommand() {
    if (syncCommandsMethod == null) {
      return;
    }

    try {
      syncCommandsMethod.invoke(Bukkit.getServer());
    } catch (IllegalAccessException | InvocationTargetException e) {
      Bukkit.getLogger().log(Level.WARNING, "Error when syncing commands", e);
    }
  }

  /**
   * Unregister a command from the known commands
   *
   * @param command the command
   *
   * @throws IllegalAccessException if this can not get the known commands
   */
  public static void unregisterFromKnownCommands(@NotNull final Command command) throws IllegalAccessException {
    Map<?, ?> knownCommands = (Map<?, ?>) knownCommandsField.get(bukkitCommandMap);
    knownCommands.values().removeIf(command::equals);
    command.unregister(bukkitCommandMap);
  }

  /**
   * Register the command to the command map
   *
   * @param label   the label of the command
   * @param command the command
   */
  public static void registerCommandToCommandMap(@NotNull final String label, @NotNull final Command command) {
    bukkitCommandMap.register(label, command);
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

    registerCommandToCommandMap(this.plugin.getName(), command);
    this.registered.put(name, command);
  }

  /**
   * Unregister the command
   *
   * @param command the command object
   */
  public final void unregister(@NotNull final Command command) {
    try {
      unregisterFromKnownCommands(command);
      this.registered.remove(command.getLabel());
    } catch (ReflectiveOperationException e) {
      this.plugin.getLogger().log(Level.WARNING, "Something wrong when unregister the command", e);
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
   * Unregister all commands
   */
  public final void unregisterAll() {
    this.registered.values().forEach(command -> {
      try {
        unregisterFromKnownCommands(command);
      } catch (ReflectiveOperationException e) {
        this.plugin.getLogger().log(Level.WARNING, "Something wrong when unregister the command", e);
      }
    });
    this.registered.clear();
  }

  /**
   * Get registered commands
   *
   * @return the map contains the name and the command object
   */
  @NotNull
  public final Map<String, Command> getRegistered() {
    return Collections.unmodifiableMap(this.registered);
  }
}
