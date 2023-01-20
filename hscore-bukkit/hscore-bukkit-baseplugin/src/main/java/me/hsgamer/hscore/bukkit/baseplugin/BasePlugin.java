package me.hsgamer.hscore.bukkit.baseplugin;

import me.hsgamer.hscore.bukkit.command.CommandManager;
import me.hsgamer.hscore.bukkit.simpleplugin.SimplePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import static java.util.logging.Level.WARNING;

/**
 * A slightly complicated {@link SimplePlugin} implementation
 */
public class BasePlugin extends SimplePlugin {
  private final CommandManager commandManager = new CommandManager(this);
  private final Set<Permission> registeredPermissions = new HashSet<>();

  public BasePlugin() {
    super();
  }

  protected BasePlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
    super(loader, description, dataFolder, file);
  }

  @Override
  public final void onLoad() {
    super.onLoad();
  }

  @Override
  public final void onEnable() {
    super.onEnable();
    registerPermissions();
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, CommandManager::syncCommand);
  }

  @Override
  public final void onDisable() {
    super.onDisable();
    commandManager.unregisterAll();
    registeredPermissions.forEach(Bukkit.getPluginManager()::removePermission);
  }

  private void registerPermissions() {
    Class<?> clazz = getPermissionClass();
    if (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        int modifiers = field.getModifiers();
        if (!field.getType().equals(Permission.class) || !Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers))
          continue;

        try {
          Permission permission = (Permission) field.get(null);
          registerPermission(permission);
        } catch (IllegalAccessException e) {
          getLogger().log(WARNING, "Failed to register permission", e);
        }
      }
    }
  }

  /**
   * Get the class containing all the {@link Permission}s.
   * The permissions must be static, public, and {@link Permission} type.
   *
   * @return the class
   */
  protected Class<?> getPermissionClass() {
    return getClass();
  }

  /**
   * Register a {@link Permission}
   *
   * @param permission the permission
   */
  public void registerPermission(Permission permission) {
    Bukkit.getPluginManager().addPermission(permission);
    registeredPermissions.add(permission);
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
