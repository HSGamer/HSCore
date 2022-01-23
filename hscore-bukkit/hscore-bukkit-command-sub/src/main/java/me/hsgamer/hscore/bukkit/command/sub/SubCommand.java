package me.hsgamer.hscore.bukkit.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * A sub-command
 */
public abstract class SubCommand {

  protected final String permission;
  protected final String usage;
  protected final String description;
  protected final boolean consoleAllowed;
  protected final String name;
  protected Consumer<CommandSender> usageSender = sender -> {
    String colored = ChatColor.translateAlternateColorCodes('&', getUsage());
    sender.sendMessage(colored);
  };
  protected Consumer<CommandSender> playerOnlyMessageSender = sender -> sender.sendMessage(ChatColor.RED + "You have to be a player to do this");
  protected Consumer<CommandSender> noPermissionMessageSender = sender -> sender.sendMessage(ChatColor.RED + "You don't have permission to do this");

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
   *
   * @return whether the command runs successfully
   */
  public final boolean onCommand(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    if (sender instanceof ConsoleCommandSender && !consoleAllowed) {
      playerOnlyMessageSender.accept(sender);
      return false;
    }

    if (permission != null && !sender.hasPermission(permission)) {
      noPermissionMessageSender.accept(sender);
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
   *
   * @return true if the sender is
   */
  public abstract boolean isProperUsage(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args);

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
  public abstract List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args);
}
