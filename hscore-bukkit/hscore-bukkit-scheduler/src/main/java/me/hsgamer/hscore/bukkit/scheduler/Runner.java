package me.hsgamer.hscore.bukkit.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

/**
 * The runner as a part of {@link Scheduler}
 */
public interface Runner {
  /**
   * Run a task
   *
   * @param plugin   the plugin that owns the task
   * @param runnable the task
   *
   * @return the task
   */
  Task runTask(Plugin plugin, Runnable runnable);

  /**
   * Run a delayed task
   *
   * @param plugin   the plugin that owns the task
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   *
   * @return the task
   */
  Task runTaskLater(Plugin plugin, Runnable runnable, long delay);

  /**
   * Run a task repeatedly
   *
   * @param plugin   the plugin that owns the task
   * @param runnable the task. Return true to continue, false to stop.
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  Task runTaskTimer(Plugin plugin, BooleanSupplier runnable, long delay, long period);

  /**
   * Run a task repeatedly
   *
   * @param plugin   the plugin that owns the task
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
    return runTaskTimer(plugin, () -> {
      runnable.run();
      return true;
    }, delay, period);
  }

  /**
   * Run a task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   *
   * @return the task
   */
  Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired);

  /**
   * Run a delayed task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   *
   * @return the task
   */
  Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay);

  /**
   * Run a task repeatedly related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task. Return true to continue, false to stop.
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  Task runEntityTaskTimer(Plugin plugin, Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period);

  /**
   * Run a task repeatedly related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runEntityTaskTimer(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, long period) {
    return runEntityTaskTimer(plugin, entity, () -> {
      runnable.run();
      return true;
    }, retired, delay, period);
  }

  /**
   * Run a task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  default Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable) {
    return runEntityTask(plugin, entity, runnable, () -> {
    });
  }

  /**
   * Run a delayed task related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  default Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, long delay) {
    return runEntityTaskLater(plugin, entity, runnable, () -> {
    }, delay);
  }

  /**
   * Run a task repeatedly related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task. Return true to continue, false to stop.
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runEntityTaskTimer(Plugin plugin, Entity entity, BooleanSupplier runnable, long delay, long period) {
    return runEntityTaskTimer(plugin, entity, runnable, () -> {
    }, delay, period);
  }

  /**
   * Run a task repeatedly related to an entity
   *
   * @param plugin   the plugin that owns the task
   * @param entity   the entity that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  default Task runEntityTaskTimer(Plugin plugin, Entity entity, Runnable runnable, long delay, long period) {
    return runEntityTaskTimer(plugin, entity, runnable, () -> {
    }, delay, period);
  }

  /**
   * Run a task related to an entity with a finalizer.
   * The finalizer will be run both after the task is run and when the entity is retired.
   *
   * @param plugin    the plugin that owns the task
   * @param entity    the entity that the task is related to
   * @param runnable  the task
   * @param finalizer the finalizer
   *
   * @return the task
   */
  default Task runEntityTaskWithFinalizer(Plugin plugin, Entity entity, Runnable runnable, Runnable finalizer) {
    return runEntityTask(plugin, entity, () -> {
      try {
        runnable.run();
      } finally {
        finalizer.run();
      }
    }, finalizer);
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
   *
   * @return the task
   */
  default Task runEntityTaskLaterWithFinalizer(Plugin plugin, Entity entity, Runnable runnable, Runnable finalizer, long delay) {
    return runEntityTaskLater(plugin, entity, () -> {
      try {
        runnable.run();
      } finally {
        finalizer.run();
      }
    }, finalizer, delay);
  }

  /**
   * Run a task related to a location
   *
   * @param plugin   the plugin that owns the task
   * @param location the location that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  Task runLocationTask(Plugin plugin, Location location, Runnable runnable);

  /**
   * Run a delayed task related to a location
   *
   * @param plugin   the plugin that owns the task
   * @param location the location that the task is related to
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   *
   * @return the task
   */
  Task runLocationTaskLater(Plugin plugin, Location location, Runnable runnable, long delay);

  /**
   * Run a task repeatedly related to a location
   *
   * @param plugin   the plugin that owns the task
   * @param location the location that the task is related to
   * @param runnable the task. Return true to continue, false to stop.
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  Task runLocationTaskTimer(Plugin plugin, Location location, BooleanSupplier runnable, long delay, long period);

  /**
   * Run a task repeatedly related to a location
   *
   * @param plugin   the plugin that owns the task
   * @param location the location that the task is related to
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runLocationTaskTimer(Plugin plugin, Location location, Runnable runnable, long delay, long period) {
    return runLocationTaskTimer(plugin, location, () -> {
      runnable.run();
      return true;
    }, delay, period);
  }
}
