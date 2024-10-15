package me.hsgamer.hscore.bukkit.action;

import io.github.projectunified.minelib.scheduler.entity.EntityScheduler;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * The action to delay the next action
 */
public class DelayAction implements Action {
  private final Plugin plugin;
  private final String delay;

  /**
   * Create a new action
   *
   * @param plugin the plugin
   * @param delay  the delay in ticks
   */
  public DelayAction(Plugin plugin, String delay) {
    this.plugin = plugin;
    this.delay = delay;
  }

  @Override
  public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
    Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      process.next();
      return;
    }

    String value = stringReplacer.replaceOrOriginal(delay, uuid);
    long delay;
    try {
      delay = Long.parseLong(value);
    } catch (NumberFormatException e) {
      plugin.getLogger().warning("Invalid delay: " + value);
      process.next();
      return;
    }

    EntityScheduler.get(plugin, player).runLater(process::next, process::next, delay);
  }
}
