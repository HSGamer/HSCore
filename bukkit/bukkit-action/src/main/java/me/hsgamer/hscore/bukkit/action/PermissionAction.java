package me.hsgamer.hscore.bukkit.action;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The action to execute a command with permissions
 */
public class PermissionAction extends CommandAction {
  private final List<String> permissions;

  /**
   * Create a new action
   *
   * @param plugin      the plugin
   * @param command     the command
   * @param permissions the permissions
   */
  public PermissionAction(Plugin plugin, String command, List<String> permissions) {
    super(plugin, command);
    this.permissions = permissions;
  }

  @Override
  protected void accept(Player player, String command) {
    List<PermissionAttachment> attachments = permissions.stream()
      .filter(s -> !player.hasPermission(s))
      .map(s -> player.addAttachment(plugin, s, true))
      .collect(Collectors.toList());
    try {
      player.chat(command);
    } finally {
      attachments.forEach(player::removeAttachment);
    }
  }
}
