package me.hsgamer.hscore.bukkit.command.extra;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * The interface for all command nodes
 */
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
  default List<String> tabComplete(final CommandSender sender, final String... args) {
    CommandNode current = this;

    for (int i = 0; i < args.length - 1; i++) {
      String currentArg = args[i];
      Optional<CommandNode> found = current.subCommands().stream()
        .filter(node -> node.label().equalsIgnoreCase(currentArg))
        .findFirst();
      if (!found.isPresent() || current.subCommands().isEmpty()) {
        return Collections.emptyList();
      }
      current = found.get();
    }
    return current.subCommands().stream()
      .map(CommandNode::label)
      .filter(token -> token.toLowerCase().startsWith(args[args.length - 1]))
      .collect(Collectors.toList());
  }

  default CommandFeedback feedback() {
    return new CommandFeedback();
  }

  /**
   * Handle the arguments
   *
   * @param sender Source object which is executing this command
   * @param args   All arguments passed to the command, split via ' '
   *
   * @return true if the command was successful, otherwise false
   */
  default boolean handle(final CommandSender sender, final String... args) {
    CommandNode current = this;
    String[] currentArgs = args;
    if (!current.consume()) {
      while (!current.subCommands().isEmpty() && currentArgs.length > 0) {
        String label = currentArgs[0];
        currentArgs = Arrays.copyOfRange(currentArgs, 1, currentArgs.length);
        Optional<CommandNode> found = current.subCommands()
          .stream()
          .filter(subCommand -> subCommand.match(label))
          .findFirst();
        if (found.isPresent()) {
          current = found.get();
        } else {
          sender.sendMessage(feedback().INVALID_COMMAND.getFeedback()
            .replace("%arg%", currentArgs.length > 0 ? currentArgs[0] : ""));
          return false;
        }
      }
    }

    if (current.onlyPlayer() && !(sender instanceof Player)) {
      sender.sendMessage(feedback().ONLY_PLAYER.getFeedback());
      return false;
    }

    if (!current.permissions().isEmpty() && current.permissions().stream().noneMatch(sender::hasPermission)) {
      sender.sendMessage(feedback().NO_PERMISSION.getFeedback());
      return false;
    }

    if (currentArgs.length > 0 && !current.consume()) {
      sender.sendMessage(feedback().TOO_MANY_ARGUMENTS.getFeedback());
      return false;
    }

    return current.execute(sender, current.label(), currentArgs);
  }
}
