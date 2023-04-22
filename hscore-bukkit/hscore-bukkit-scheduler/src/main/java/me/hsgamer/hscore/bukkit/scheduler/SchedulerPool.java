package me.hsgamer.hscore.bukkit.scheduler;

import me.hsgamer.hscore.bukkit.folia.FoliaChecker;
import me.hsgamer.hscore.bukkit.scheduler.bukkit.BukkitScheduler;
import me.hsgamer.hscore.bukkit.scheduler.folia.FoliaScheduler;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The pool to store the {@link Scheduler} object for each {@link Plugin}
 */
class SchedulerPool {
  private static final Map<Plugin, Scheduler> SCHEDULER_MAP = new ConcurrentHashMap<>();

  /**
   * Get the {@link Scheduler} for the given {@link Plugin}
   *
   * @param plugin the plugin
   *
   * @return the scheduler
   */
  static Scheduler plugin(Plugin plugin) {
    return SCHEDULER_MAP.computeIfAbsent(plugin, newPlugin -> {
      if (FoliaChecker.isFolia()) {
        return new FoliaScheduler(newPlugin);
      } else {
        return new BukkitScheduler(newPlugin);
      }
    });
  }
}
