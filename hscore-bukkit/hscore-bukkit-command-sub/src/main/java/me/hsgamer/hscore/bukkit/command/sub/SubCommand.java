package me.hsgamer.hscore.bukkit.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * A sub-command
 */
public abstract class SubCommand {
  protected final String name;
  protected boolean consoleAllowed;
  protected String permission;
  protected String usage;
  protected String description;
  protected Consumer<CommandSender> usageSender = sender -> {
    String colored = ChatColor.translateAlternateColorCodes('&', getUsage());
    sender.sendMessage(colored);
  };
  protected Consumer<CommandSender> playerOnlyMessageSender = sender -> sender.sendMessage(ChatColor.RED + "You have to be a player to do this");
  protected Consumer<CommandSender> noPermissionMessageSender = sender -> sender.sendMessage(ChatColor.RED + "You don't have permission to do this");

  /**
   * Create new sub-command
   *
   * @param name the name
   */
  protected SubCommand(@NotNull String name) {
    this.name = name;
    consoleAllowed = true;
    permission = null;
    usage = "";
    description = "";
  }

  /**
   * Create new sub-command
   *
   * @param name           the name
   * @param description    the description
   * @param usage          the usage
   * @param permission     the permission
   * @param consoleAllowed is console allowed to use?
   */
  protected SubCommand(@NotNull final String name, @NotNull final String description, @NotNull final String usage, @Nullable final String permission, final boolean consoleAllowed) {
    this.name = name;
    this.permission = permission;
    this.description = description;
    this.usage = usage;
    this.consoleAllowed = consoleAllowed;
  }

  /**
   * Check the command is executable for the sender
   *
   * @param sender             the sender
   * @param sendMessageIfFalse true if the message should be sent if the command is not executable
   *
   * @return true if the command is executable
   */
  public final boolean isExecutable(@NotNull final CommandSender sender, boolean sendMessageIfFalse) {
    if (sender instanceof ConsoleCommandSender && !consoleAllowed) {
      if (sendMessageIfFalse) {
        playerOnlyMessageSender.accept(sender);
      }
      return false;
    }

    if (permission != null && !sender.hasPermission(permission)) {
      if (sendMessageIfFalse) {
        noPermissionMessageSender.accept(sender);
      }
      return false;
    }

    return true;
  }

  /**
   * Execute the sub-command
   *
   * @param sender the sender
   * @param label  the label of the parent command
   * @param args   the arguments
   *
   * @return whether the command runs successfully
   */
  public final boolean onCommand(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    if (!isExecutable(sender, true)) {
      return false;
    }

    if (!isProperUsage(sender, label, args)) {
      usageSender.accept(sender);
      return false;
    }

    onSubCommand(sender, label, args);
    return true;
  }

  /**
   * Get the description
   *
   * @return the description
   */
  @NotNull
  public final String getDescription() {
    return description;
  }

  /**
   * Set the description
   *
   * @param description the description
   */
  public final void setDescription(@NotNull String description) {
    this.description = description;
  }

  /**
   * Get the usage
   *
   * @return the usage
   */
  @NotNull
  public final String getUsage() {
    return usage;
  }

  /**
   * Set the usage
   *
   * @param usage the usage
   */
  public final void setUsage(@NotNull String usage) {
    this.usage = usage;
  }

  /**
   * Get the permission
   *
   * @return the permission
   */
  @Nullable
  public final String getPermission() {
    return permission;
  }

  /**
   * Set the permission
   *
   * @param permission the permission
   */
  public final void setPermission(@Nullable String permission) {
    this.permission = permission;
  }

  /**
   * Set the permission
   *
   * @param permission the permission
   */
  public final void setPermission(@NotNull Permission permission) {
    this.permission = permission.getName();
  }

  /**
   * Check if the console is allowed to use
   *
   * @return true if it is
   */
  public final boolean isConsoleAllowed() {
    return consoleAllowed;
  }

  /**
   * Set if the console is allowed to use
   *
   * @param consoleAllowed true if it is
   */
  public final void setConsoleAllowed(boolean consoleAllowed) {
    this.consoleAllowed = consoleAllowed;
  }

  /**
   * Get the name of the sub-command
   *
   * @return the name
   */
  @NotNull
  public final String getName() {
    return name;
  }

  /**
   * Called when executing the sub-command
   *
   * @param sender the sender
   * @param label  the label of the parent command
   * @param args   the arguments
   */
  public abstract void onSubCommand(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args);

  /**
   * Check if the sender properly calls the sub-command
   *
   * @param sender the sender
   * @param label  the label of the parent command
   * @param args   the arguments
   *
   * @return true if the sender is
   */
  public boolean isProperUsage(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    return true;
  }

  /**
   * Get the suggested strings when the sender use TAB key to complete the sub-command
   *
   * @param sender the sender
   * @param label  the label of the parent command
   * @param args   the arguments
   *
   * @return the suggested strings
   */
  @NotNull
  public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    return Collections.emptyList();
  }
}
