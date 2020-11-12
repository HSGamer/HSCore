package me.hsgamer.hscore.bukkit.command.extra;

import java.util.function.Supplier;

public class CommandFeedback {
  public static final CommandFeedback UNKNOWN_COMMAND = new CommandFeedback("§cUnknown command");
  public static final CommandFeedback ONLY_PLAYER = new CommandFeedback("§cOnly player can use this command");
  public static final CommandFeedback NO_PERMISSION = new CommandFeedback("§cYou don't have permission to do this");
  public static final CommandFeedback TOO_MANY_ARGUMENTS = new CommandFeedback("§cToo many arguments");

  private Supplier<String> feedback;

  private CommandFeedback(Supplier<String> feedback) {
    this.feedback = feedback;
  }

  private CommandFeedback(String string) {
    this(() -> string);
  }

  public String getFeedback() {
    return feedback.get();
  }

  public void setFeedback(Supplier<String> feedback) {
    this.feedback = feedback;
  }

  public void setFeedback(String feedback) {
    setFeedback(() -> feedback);
  }
}
