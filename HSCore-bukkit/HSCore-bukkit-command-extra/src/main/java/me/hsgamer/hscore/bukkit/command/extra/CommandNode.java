package me.hsgamer.hscore.bukkit.command.extra;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public interface CommandNode {

  BiPredicate<CommandNode, String> MATCH_LABEL = (node, text) -> node.label().equalsIgnoreCase(text);
  BiPredicate<CommandNode, String> MATCH_ALIASES = (node, text) -> node.aliases().stream().anyMatch(text::equalsIgnoreCase);

  /**
   * Get the label of command
   *
   * @return the label
   */
  String label();


  /**
   * Get the aliases of command
   *
   * @return the aliases, default is empty
   */
  default List<String> aliases() {
    return Collections.emptyList();
  }

  /**
   * Get the permissions of command
   *
   * @return the permissions, default is empty
   */
  default List<String> permissions() {
    return Collections.emptyList();
  }

  /**
   * Check if only the players can use this command
   *
   * @return true if they can
   */
  default boolean onlyPlayer() {
    return false;
  }

  /**
   * Executes the command when there is no subcommand or no args or consumes args, returning its success
   *
   * @param sender Source object which is executing this command
   * @param label  The alias of the command used
   * @param args   All arguments passed to the command, split via ' '
   *
   * @return true if the command was successful, otherwise false
   */
  boolean execute(final CommandSender sender, final String label, final String[] args);

  /**
   * Check if this command consumes arguments
   *
   * @return true if it does
   */
  default boolean consume() {
    return false;
  }

  /**
   * Check if the label match the condition
   *
   * @param text the label
   *
   * @return true if it is
   */
  default boolean match(final String text) {
    return MATCH_LABEL.test(this, text) || MATCH_ALIASES.test(this, text);
  }

  /**
   * Get a list of subcommand
   *
   * @return subcommands, default is empty
   */
  default List<CommandNode> subCommands() {
    return Collections.emptyList();
  }

  /**
   * Executed on tab completion for this command, returning a list of
   * options the player can tab through.
   *
   * @param sender Source object which is executing this command
   * @param args   All arguments passed to the command, split via ' '
   *
   * @return a list of tab-completions for the specified arguments. This
   * will never be null. List may be immutable.
   */
  default List<String> tabComplete(final CommandSender sender, final String[] args) {
    CommandNode current = this;
    String[] currentArgs = args;
    while (!current.subCommands().isEmpty() && currentArgs.length > 1) {
      String incompleteWord = currentArgs[0].toLowerCase();
      Optional<CommandNode> found = current.subCommands().stream()
        .filter(subCommand ->
          (
            !subCommand.onlyPlayer() ||
              sender instanceof Player
          ) && (
            subCommand.permissions().isEmpty() ||
              subCommand.permissions().stream().anyMatch(sender::hasPermission)
          ) && (
            subCommand.label().toLowerCase().startsWith(incompleteWord) ||
              subCommand.aliases().stream().anyMatch(
                alias -> alias.toLowerCase().startsWith(incompleteWord)
              )
          )
        ).findFirst();
      if (found.isPresent()) {
        current = found.get();
        currentArgs = Arrays.copyOfRange(currentArgs, 1, currentArgs.length);
      } else return Collections.emptyList();
    }
    return current.subCommands().stream().map(CommandNode::label).collect(Collectors.toList());
  }

  /**
   * Handle the arguments
   *
   * @param sender Source object which is executing this command
   * @param args   All arguments passed to the command, split via ' '
   *
   * @return true if the command was successful, otherwise false
   */
  default boolean handle(final CommandSender sender, final String[] args) {
    CommandNode current = this;
    String[] currentArgs = args;
    while (!current.subCommands().isEmpty() && currentArgs.length > 0) {
      if (current.consume()) break;
      String label = currentArgs[0];
      currentArgs = Arrays.copyOfRange(currentArgs, 1, currentArgs.length);
      Optional<CommandNode> found = current.subCommands().stream()
        .filter(subCommand ->
          subCommand.match(label)
        ).findFirst();
      if (found.isPresent()) {
        current = found.get();
      } else {
        sender.sendMessage(CommandFeedback.UNKNOWN_COMMAND.getFeedback());
        return false;
      }
    }

    if (current.onlyPlayer() && !(sender instanceof Player)) {
      sender.sendMessage(CommandFeedback.ONLY_PLAYER.getFeedback());
      return false;
    }

    if (!current.permissions().isEmpty() && current.permissions().stream().noneMatch(sender::hasPermission)) {
      sender.sendMessage(CommandFeedback.NO_PERMISSION.getFeedback());
      return false;
    }

    if (currentArgs.length > 0 && !current.consume()) {
      sender.sendMessage(CommandFeedback.TOO_MANY_ARGUMENTS.getFeedback());
      return false;
    }

    return current.execute(sender, current.label(), currentArgs);
  }
}
