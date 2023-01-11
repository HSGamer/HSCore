package me.hsgamer.hscore.bukkit.command.sub;

import me.hsgamer.hscore.collections.map.CaseInsensitiveStringHashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The sub-command manager
 */
public class SubCommandManager {

  protected static final String HELP = "help";
  protected final Map<String, SubCommand> subcommands = new CaseInsensitiveStringHashMap<>();

  /**
   * Execute the command
   *
   * @param sender the sender
   * @param label  the label
   * @param args   the arguments
   *
   * @return whether the command runs successfully
   */
  public final boolean onCommand(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    if (args.length < 1 || args[0].equalsIgnoreCase(HELP)) {
      sendHelpMessage(sender, label, args);
      return true;
    } else if (!subcommands.containsKey(args[0])) {
      sendArgNotFoundMessage(sender, label, args);
      return false;
    } else {
      return subcommands.get(args[0]).onCommand(sender, label, Arrays.copyOfRange(args, 1, args.length));
    }
  }

  /**
   * Send help command
   *
   * @param sender the sender
   * @param label  the label
   * @param args   the arguments
   */
  public void sendHelpMessage(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    for (SubCommand subcommand : subcommands.values()) {
      sender.sendMessage(ChatColor.YELLOW + subcommand.usage.replace("<label>", label));
      sender.sendMessage(ChatColor.WHITE + "  " + subcommand.description);
    }
  }

  /**
   * Send "Argument Not Found" message
   *
   * @param sender the sender
   * @param label  the label
   * @param args   the arguments
   */
  public void sendArgNotFoundMessage(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    sender.sendMessage(ChatColor.RED + "Argument not found");
  }

  /**
   * Register a sub-command
   *
   * @param subCommand the sub-command
   */
  public final void registerSubcommand(@NotNull final SubCommand subCommand) {
    if (subCommand.name.equalsIgnoreCase(HELP)) {
      throw new IllegalArgumentException("'" + HELP + "' is a predefined argument");
    }
    subcommands.put(subCommand.getName(), subCommand);
  }

  /**
   * Unregister a sub-command
   *
   * @param name the name of the sub-command
   */
  public final void unregisterSubcommand(@NotNull final String name) {
    subcommands.remove(name);
  }

  /**
   * Unregister a sub-command
   *
   * @param subCommand the sub-command
   */
  public final void unregisterSubcommand(@NotNull final SubCommand subCommand) {
    subcommands.remove(subCommand.getName());
  }

  /**
   * Get the available sub-commands
   *
   * @return the unmodifiable map of sub-commands
   */
  @NotNull
  public final Map<String, SubCommand> getSubcommands() {
    return Collections.unmodifiableMap(subcommands);
  }

  /**
   * Get the suggested strings when the sender use TAB key to complete the command
   *
   * @param sender the sender
   * @param label  the label
   * @param args   the arguments
   *
   * @return the suggested strings
   */
  public final List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String... args) {
    List<String> list = new ArrayList<>();
    if (args.length < 1 || args[0].isEmpty()) {
      list.add(HELP);
      list.addAll(subcommands.keySet());
    } else if (subcommands.containsKey(args[0])) {
      SubCommand subCommand = subcommands.get(args[0]);
      if (subCommand.isExecutable(sender, false)) {
        list.addAll(subCommand.onTabComplete(sender, label, Arrays.copyOfRange(args, 1, args.length)));
      }
    } else {
      for (String subLabel : subcommands.keySet()) {
        if (subLabel.startsWith(args[0])) {
          list.add(subLabel);
        }
      }
      if (HELP.startsWith(args[0])) {
        list.add(HELP);
      }
    }
    return list;
  }
}
