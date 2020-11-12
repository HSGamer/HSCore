package me.hsgamer.hscore.bukkit.command.extra;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CommandNode {
  String label();

  default List<String> aliases() {
    return Collections.emptyList();
  }

  default List<String> permissions() {
    return Collections.emptyList();
  }

  default boolean onlyPlayer() {
    return false;
  }

  boolean execute(CommandSender sender, String label);

  default boolean match(String label) {
    return label().equalsIgnoreCase(label) || aliases().stream().anyMatch(label::equalsIgnoreCase);
  }

  default boolean startsWithMatch(String incompleteWord) {
    return label().toLowerCase().startsWith(incompleteWord.toLowerCase()) || aliases().stream().anyMatch(alias -> alias.toLowerCase().startsWith(incompleteWord.toLowerCase()));
  }

  default List<CommandNode> subCommands() {
    return Collections.emptyList();
  }

  default List<String> tabComplete(CommandSender sender, String[] args) {
    CommandNode child = this;
    while (!child.subCommands().isEmpty() && args.length > 1) {
      String incompleteWord = args[0];
      Optional<CommandNode> found = child.subCommands().stream()
        .filter(sub ->
          (!sub.onlyPlayer() || sender instanceof Player) &&
            (
              sub.permissions().isEmpty() ||
                sub.permissions().stream().anyMatch(sender::hasPermission)
            ) &&
            sub.startsWithMatch(incompleteWord)).findFirst();
      if (found.isPresent()) {
        child = found.get();
        args = Arrays.copyOfRange(args, 1, args.length);
      } else return Collections.emptyList();
    }
    return child.subCommands().stream().map(CommandNode::label).collect(Collectors.toList());
  }

  default boolean run(CommandSender sender, String[] args) {
    CommandNode currentCommandNode = this;
    while (!currentCommandNode.subCommands().isEmpty() && args.length > 0) {
      String label = args[0];
      args = Arrays.copyOfRange(args, 1, args.length);
      Optional<CommandNode> found = currentCommandNode.subCommands().stream()
        .filter(subCommand -> subCommand.match(label)).findFirst();
      if (found.isPresent()) {
        currentCommandNode = found.get();
      } else {
        sender.sendMessage(CommandFeedback.UNKNOWN_COMMAND.getFeedback());
        return false;
      }
    }

    if (currentCommandNode.onlyPlayer() && !(sender instanceof Player)) {
      sender.sendMessage(CommandFeedback.ONLY_PLAYER.getFeedback());
      return false;
    }

    if (!currentCommandNode.permissions().isEmpty() && currentCommandNode.permissions().stream().noneMatch(sender::hasPermission)) {
      sender.sendMessage(CommandFeedback.NO_PERMISSION.getFeedback());
      return false;
    }

    if (args.length > 0) {
      sender.sendMessage(CommandFeedback.TOO_MANY_ARGUMENTS.getFeedback());
      return false;
    }

    return currentCommandNode.execute(sender, currentCommandNode.label());
  }
}
