package me.hsgamer.hscore.bukkit.subcommand;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A sub-command
 */
public abstract class SubCommand {

  protected final String permission;
  protected final String usage;
  protected final String description;
  protected final boolean consoleAllowed;
  protected final String name;
  protected String playerOnlyMessage = "&cYou have to be a player to do this";
  protected String noPermissionMessage = "&cYou don't have the permission to do this";

  /**
   * Create new sub-command
   *
   * @param name           the name
   * @param description    the description
   * @param usage          the usage
   * @param permission     the permission
   * @param consoleAllowed is console allowed to use?
   */
  public SubCommand(@NotNull final String name, @NotNull final String description, @NotNull final String usage, @Nullable final String permission,
                    final boolean consoleAllowed) {
    this.name = name;
    this.permission = permission;
    this.description = description;
    this.usage = usage;
    this.consoleAllowed = consoleAllowed;
  }

  /**
   * Execute the sub-command
   *
   * @param sender the sender
   * @param label  the label of the parent command
   * @param args   the arguments
   * @return whether the command runs successfully
   */
  public final boolean onCommand(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    if (sender instanceof ConsoleCommandSender && !consoleAllowed) {
      MessageUtils.sendMessage(sender, playerOnlyMessage);
      return false;
    }

    if (permission != null && !sender.hasPermission(permission)) {
      MessageUtils.sendMessage(sender, noPermissionMessage);
      return false;
    }

    if (!isProperUsage(sender, label, args)) {
      MessageUtils.sendMessage(sender, usage);
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
   * Get the usage
   *
   * @return the usage
   */
  @NotNull
  public final String getUsage() {
    return usage;
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
   * Check if the console is allowed to use
   *
   * @return true if it is
   */
  public final boolean isConsoleAllowed() {
    return consoleAllowed;
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
   * @return true if the sender is
   */
  public abstract boolean isProperUsage(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args);

  /**
   * Get the suggested strings when the sender use TAB key to complete the sub-command
   *
   * @param sender the sender
   * @param label  the label of the parent command
   * @param args   the arguments
   * @return the suggested strings
   */
  @NotNull
  public abstract List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args);
}
