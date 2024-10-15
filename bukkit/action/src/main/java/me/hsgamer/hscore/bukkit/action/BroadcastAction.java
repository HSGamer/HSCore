package me.hsgamer.hscore.bukkit.action;

import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * The action to broadcast a message
 */
public class BroadcastAction implements Action {
  private final String message;

  /**
   * Create a new action
   *
   * @param message the message
   */
  public BroadcastAction(String message) {
    this.message = message;
  }

  @Override
  public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
    String replaced = stringReplacer.replaceOrOriginal(message, uuid);
    Bukkit.broadcastMessage(replaced);
    process.next();
  }
}
