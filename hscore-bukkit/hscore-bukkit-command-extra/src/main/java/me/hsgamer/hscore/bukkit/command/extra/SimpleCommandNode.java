package me.hsgamer.hscore.bukkit.command.extra;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * A simple implementation of {@link CommandNode}
 */
public class SimpleCommandNode implements CommandNode {

  private final List<String> aliases;
  private final List<String> permissions;
  private final Predicate<String> match;
  private final CommandExecutor execute;
  private String label;
  private CommandFeedback feedback;

  /**
   * Create a {@link CommandNode} in simple way
   *
   * @param match       the match predicate
   * @param permissions the permissions
   * @param execute     the execute predicate
   */
  public SimpleCommandNode(Predicate<String> match, List<String> permissions, CommandExecutor execute) {
    this.label = "";
    this.match = match;
    this.permissions = permissions;
    this.aliases = Collections.emptyList();
    this.execute = execute;
    this.feedback = new CommandFeedback();
  }

  public SimpleCommandNode(Predicate<String> match, String permission, CommandExecutor execute) {
    this(match, Collections.singletonList(permission), execute);
  }

  public SimpleCommandNode(Predicate<String> match, CommandExecutor execute) {
    this(match, Collections.emptyList(), execute);
  }

  /**
   * Create a {@link CommandNode} in simple way
   *
   * @param label       the label
   * @param aliases     the aliases
   * @param permissions the permissions
   * @param execute     the execute predicate
   */
  public SimpleCommandNode(String label, List<String> aliases, List<String> permissions, CommandExecutor execute) {
    this.label = label;
    this.match = matchLabel -> MATCH_LABEL.test(this, matchLabel) || MATCH_ALIASES.test(this, matchLabel);
    this.permissions = permissions;
    this.aliases = aliases;
    this.execute = execute;
    this.feedback = new CommandFeedback();
  }

  public SimpleCommandNode(String label, List<String> aliases, String permission, CommandExecutor execute) {
    this(label, aliases, Collections.singletonList(permission), execute);
  }

  public SimpleCommandNode(String label, String alias, String permission, CommandExecutor execute) {
    this(label, Collections.singletonList(alias), Collections.singletonList(permission), execute);
  }

  public SimpleCommandNode(String label, String permission, CommandExecutor execute) {
    this(label, Collections.emptyList(), permission, execute);
  }

  public SimpleCommandNode(String label, CommandExecutor execute) {
    this(label, Collections.emptyList(), Collections.emptyList(), execute);
  }

  public void setFeedback(CommandFeedback feedback) {
    this.feedback = feedback;
  }

  @Override
  public String label() {
    return label;
  }

  @Override
  public List<String> aliases() {
    return aliases;
  }

  @Override
  public List<String> permissions() {
    return permissions;
  }

  @Override
  public boolean match(String label) {
    boolean matched = match.test(label);
    this.label = matched ? label : this.label;
    return matched;
  }

  @Override
  public CommandFeedback feedback() {
    return this.feedback;
  }

  @Override
  public boolean execute(CommandSender sender, String label, String... args) {
    return execute.execute(sender, label, args);
  }
}
