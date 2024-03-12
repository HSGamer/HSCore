package me.hsgamer.hscore.bukkit.action;

import io.github.projectunified.minelib.scheduler.global.GlobalScheduler;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * The action to execute a command as console
 */
public class ConsoleAction implements Action {
  private final Plugin plugin;
  private final String command;

  /**
   * Create a new action
   *
   * @param plugin  the plugin
   * @param command the command
   */
  public ConsoleAction(Plugin plugin, String command) {
    this.plugin = plugin;
    this.command = command;
  }

  @Override
  public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
    GlobalScheduler.get(plugin).run(() -> {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), stringReplacer.replaceOrOriginal(command, uuid));
      process.next();
    });
  }
}
