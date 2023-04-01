package me.hsgamer.hscore.bukkit.baseplugin;

import me.hsgamer.hscore.bukkit.command.CommandManager;
import me.hsgamer.hscore.bukkit.simpleplugin.SimplePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.permissions.Permission;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

  @Override
  public final void onLoad() {
    super.onLoad();
  }

  @Override
  public final void onEnable() {
    super.onEnable();
    registerPermissions();
    addPostEnableRunnable(CommandManager::syncCommand);
  }

  @Override
  public final void onDisable() {
    super.onDisable();
    commandManager.unregisterAll();
    registeredPermissions.forEach(Bukkit.getPluginManager()::removePermission);
  }

  private void registerPermissions() {
    for (Class<?> clazz : getPermissionClasses()) {
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
   * Get the list of classes containing all the {@link Permission}s.
   * The permissions must be static, public, and {@link Permission} type.
   *
   * @return the class
   */
  protected List<Class<?>> getPermissionClasses() {
    return Collections.singletonList(getClass());
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
