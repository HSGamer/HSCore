package me.hsgamer.hscore.bukkit.subcommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.map.CaseInsensitiveStringMap;
import org.bukkit.command.CommandSender;

/**
 * The sub-command manager
 */
public class SubCommandManager {

  private static final String HELP = "help";

  protected final Map<String, SubCommand> subcommands = new CaseInsensitiveStringMap<>();
  protected Supplier<List<String>> helpHeader = Collections::emptyList;
  protected Supplier<List<String>> helpInfo = () -> Arrays.asList(
      "&e/<label> <subcommand>",
      "    &b<description>"
  );
  protected Supplier<List<String>> helpFooter = Collections::emptyList;

  /**
   * Execute the command
   *
   * @param sender the sender
   * @param label  the label
   * @param args   the arguments
   * @return whether the command runs successfully
   */
  public boolean onCommand(CommandSender sender, String label, String[] args) {
    if (args.length < 1 || args[0].equalsIgnoreCase(HELP) || !subcommands.containsKey(args[0])) {
      List<String> list = new ArrayList<>(helpHeader.get());
      for (Map.Entry<String, SubCommand> entry : subcommands.entrySet()) {
        SubCommand subCommand = entry.getValue();
        for (String string : helpInfo.get()) {
          list.add(string
              .replace("<subcommand>", entry.getKey())
              .replace("<description>", subCommand.getDescription())
              .replace("<label>", label)
              .replace("<usage>", subCommand.getUsage())
              .replace("<permission>", subCommand.getPermission())
              .replace("<consoleAllowed>", String.valueOf(subCommand.isConsoleAllowed()))
          );
        }
      }
      list.addAll(helpFooter.get());
      list.forEach(s -> MessageUtils.sendMessage(sender, s));
      return true;
    } else {
      return subcommands.get(args[0])
          .onCommand(sender, label, Arrays.copyOfRange(args, 1, args.length));
    }
  }

  /**
   * Register a sub-command
   *
   * @param subCommand the sub-command
   */
  public void registerSubcommand(SubCommand subCommand) {
    subcommands.put(subCommand.getName(), subCommand);
  }

  /**
   * Unregister a sub-command
   *
   * @param name the name of the sub-command
   */
  public void unregisterSubcommand(String name) {
    subcommands.remove(name);
  }

  /**
   * Unregister a sub-command
   *
   * @param subCommand the sub-command
   */
  public void unregisterSubcommand(SubCommand subCommand) {
    subcommands.remove(subCommand.getName());
  }

  /**
   * Get the available sub-commands
   *
   * @return the unmodifiable map of sub-commands
   */
  public Map<String, SubCommand> getSubcommands() {
    return Collections.unmodifiableMap(subcommands);
  }

  /**
   * Get the suggested strings when the sender use TAB key to complete the command
   *
   * @param sender the sender
   * @param label  the label
   * @param args   the arguments
   * @return the suggested strings
   */
  public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
    List<String> list = new ArrayList<>();
    if (args.length < 1 || args[0].equals("")) {
      list.add(HELP);
      list.addAll(subcommands.keySet());
    } else if (subcommands.containsKey(args[0])) {
      list = subcommands.get(args[0])
          .onTabComplete(sender, label, Arrays.copyOfRange(args, 1, args.length));
    } else {
      for (String subcommand : subcommands.keySet()) {
        if (subcommand.startsWith(args[0])) {
          list.add(subcommand);
        }
      }

      if (HELP.startsWith(args[0])) {
        list.add(HELP);
      }
    }
    return list;
  }
}
