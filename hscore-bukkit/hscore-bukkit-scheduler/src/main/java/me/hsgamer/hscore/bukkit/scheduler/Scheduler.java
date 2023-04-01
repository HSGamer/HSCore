package me.hsgamer.hscore.bukkit.scheduler;

import me.hsgamer.hscore.bukkit.scheduler.bukkit.BukkitScheduler;
import me.hsgamer.hscore.bukkit.scheduler.folia.FoliaScheduler;
import org.bukkit.entity.Entity;
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
    try {
      Class.forName("io.papermc.paper.threadedregions.scheduler.AsyncScheduler");
      return new FoliaScheduler();
    } catch (Throwable throwable) {
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
   * Run a task
   *
   * @param plugin   the plugin that owns the task
   * @param runnable the task
   * @param async    whether the task is async
   *
   * @return the task
   */
  Task runTask(Plugin plugin, Runnable runnable, boolean async);

  /**
   * Run a delayed task
   *
   * @param plugin   the plugin that owns the task
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   * @param async    whether the task is async
   *
   * @return the task
   */
  Task runTaskLater(Plugin plugin, Runnable runnable, long delay, boolean async);

  /**
   * Run a task repeatedly
   *
   * @param plugin   the plugin that owns the task
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   * @param async    whether the task is async
   *
   * @return the task
   */
  Task runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period, boolean async);

  /**
   * Run a task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param async    whether the task is async
   *
   * @return the task
   */
  Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, boolean async);

  /**
   * Run a delayed task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   * @param async    whether the task is async
   *
   * @return the task
   */
  Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, boolean async);

  /**
   * Run a task repeatedly related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   * @param async    whether the task is async
   *
   * @return the task
   */
  Task runEntityTaskTimer(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, long period, boolean async);

  /**
   * Run a task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param async    whether the task is async
   *
   * @return the task
   */
  default Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, boolean async) {
    return runEntityTask(plugin, entity, runnable, () -> {
    }, async);
  }

  /**
   * Run a delayed task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param async    whether the task is async
   *
   * @return the task
   */
  default Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, long delay, boolean async) {
    return runEntityTaskLater(plugin, entity, runnable, () -> {
    }, delay, async);
  }

  /**
   * Run a task repeatedly related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param async    whether the task is async
   *
   * @return the task
   */
  default Task runEntityTaskTimer(Plugin plugin, Entity entity, Runnable runnable, long delay, long period, boolean async) {
    return runEntityTaskTimer(plugin, entity, runnable, () -> {
    }, delay, period, async);
  }

  /**
   * Run a task related to an entity with a finalizer.
   * The finalizer will be run both after the task is run and when the entity is retired.
   *
   * @param plugin    the plugin that owns the task
   * @param entity    the entity that the task is related to
   * @param runnable  the task
   * @param finalizer the finalizer
   * @param async     whether the task is async
   *
   * @return the task
   */
  default Task runEntityTaskWithFinalizer(Plugin plugin, Entity entity, Runnable runnable, Runnable finalizer, boolean async) {
    return runEntityTask(plugin, entity, () -> {
      try {
        runnable.run();
      } finally {
        finalizer.run();
      }
    }, finalizer, async);
  }

  /**
   * Run a delayed task related to an entity with a finalizer.
   * The finalizer will be run both after the task is run and when the entity is retired.
   *
   * @param plugin    the plugin that owns the task
   * @param entity    the entity that the task is related to
   * @param runnable  the task
   * @param finalizer the finalizer
   * @param delay     the delay in ticks before the task is run
   * @param async     whether the task is async
   *
   * @return the task
   */
  default Task runEntityTaskLaterWithFinalizer(Plugin plugin, Entity entity, Runnable runnable, Runnable finalizer, long delay, boolean async) {
    return runEntityTaskLater(plugin, entity, () -> {
      try {
        runnable.run();
      } finally {
        finalizer.run();
      }
    }, finalizer, delay, async);
  }
}
