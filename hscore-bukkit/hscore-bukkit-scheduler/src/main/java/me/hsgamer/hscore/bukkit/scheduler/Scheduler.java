package me.hsgamer.hscore.bukkit.scheduler;

import me.hsgamer.hscore.bukkit.folia.FoliaChecker;
import me.hsgamer.hscore.bukkit.scheduler.bukkit.BukkitScheduler;
import me.hsgamer.hscore.bukkit.scheduler.folia.FoliaScheduler;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

/**
 * The scheduler
 */
public interface Scheduler {
  /**
   * The current {@link Scheduler}.
   * Use this to get the {@link Scheduler} instead of {@link org.bukkit.Bukkit#getScheduler()}.
   */
  Scheduler CURRENT = ((Supplier<Scheduler>) () -> {
    if (FoliaChecker.isFolia()) {
      return new FoliaScheduler();
    } else {
      return new BukkitScheduler();
    }
  }).get();

  /**
   * Cancel all tasks of the plugin
   *
   * @param plugin the plugin
   */
  void cancelAllTasks(Plugin plugin);

  /**
   * Get the {@link Runner} for asynchronous tasks
   *
   * @return the {@link Runner}
   */
  Runner async();

  /**
   * Get the {@link Runner} for synchronous tasks
   *
   * @return the {@link Runner}
   */
  Runner sync();

  /**
   * Get the {@link Runner} for the given type
   *
   * @param async the type
   *
   * @return the {@link Runner}
   */
  default Runner runner(boolean async) {
    return async ? async() : sync();
  }
}
