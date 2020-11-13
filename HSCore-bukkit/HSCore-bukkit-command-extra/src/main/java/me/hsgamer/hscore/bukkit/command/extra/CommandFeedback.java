package me.hsgamer.hscore.bukkit.command.extra;

import java.util.function.Supplier;

/**
 * Feedback for {@link CommandNode}
 */
public class CommandFeedback {
  /**
   * Unknown command
   */
  public static final CommandFeedback UNKNOWN_COMMAND = new CommandFeedback("§cUnknown command");

  /**
   * Only the player can use the command
   */
  public static final CommandFeedback ONLY_PLAYER = new CommandFeedback("§cOnly player can use this command");

  /**
   * No permission
   */
  public static final CommandFeedback NO_PERMISSION = new CommandFeedback("§cYou don't have permission to do this");

  /**
   * Too many arguments for the command
   */
  public static final CommandFeedback TOO_MANY_ARGUMENTS = new CommandFeedback("§cToo many arguments");

  /**
   * Too few arguments for the command (Optional)
   */
  public static final CommandFeedback TOO_FEW_ARGUMENTS = new CommandFeedback("§cToo few arguments");

  private Supplier<String> feedback;

  private CommandFeedback(Supplier<String> feedback) {
    this.feedback = feedback;
  }

  private CommandFeedback(String string) {
    this(() -> string);
  }

  /**
   * Get the feedback
   *
   * @return the feedback
   */
  public String getFeedback() {
    return feedback.get();
  }

  /**
   * Set the feedback
   *
   * @param feedback the feedback
   */
  public void setFeedback(Supplier<String> feedback) {
    this.feedback = feedback;
  }

  /**
   * Set the feedback
   *
   * @param feedback the feedback
   */
  public void setFeedback(String feedback) {
    setFeedback(() -> feedback);
  }
}
