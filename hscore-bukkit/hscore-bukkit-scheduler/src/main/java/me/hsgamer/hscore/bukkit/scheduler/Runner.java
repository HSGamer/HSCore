package me.hsgamer.hscore.bukkit.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.BooleanSupplier;

/**
 * The runner as a part of {@link Scheduler}
 */
public interface Runner {
  /**
   * Run a task
   *
   * @param runnable the task
   *
   * @return the task
   */
  Task runTask(Runnable runnable);

  /**
   * Run a delayed task
   *
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   *
   * @return the task
   */
  Task runTaskLater(Runnable runnable, long delay);

  /**
   * Run a task repeatedly
   *
   * @param runnable the task. Return true to continue, false to stop.
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  Task runTaskTimer(BooleanSupplier runnable, long delay, long period);

  /**
   * Run a task repeatedly
   *
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runTaskTimer(Runnable runnable, long delay, long period) {
    return runTaskTimer(() -> {
      runnable.run();
      return true;
    }, delay, period);
  }

  /**
   * Run a task related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   *
   * @return the task
   */
  Task runEntityTask(Entity entity, Runnable runnable, Runnable retired);

  /**
   * Run a delayed task related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   *
   * @return the task
   */
  Task runEntityTaskLater(Entity entity, Runnable runnable, Runnable retired, long delay);

  /**
   * Run a task repeatedly related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task. Return true to continue, false to stop.
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  Task runEntityTaskTimer(Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period);

  /**
   * Run a task repeatedly related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task
   * @param retired  the task when the entity is retired (e.g. removed, invalid)
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runEntityTaskTimer(Entity entity, Runnable runnable, Runnable retired, long delay, long period) {
    return runEntityTaskTimer(entity, () -> {
      runnable.run();
      return true;
    }, retired, delay, period);
  }

  /**
   * Run a task related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  default Task runEntityTask(Entity entity, Runnable runnable) {
    return runEntityTask(entity, runnable, () -> {
    });
  }

  /**
   * Run a delayed task related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  default Task runEntityTaskLater(Entity entity, Runnable runnable, long delay) {
    return runEntityTaskLater(entity, runnable, () -> {
    }, delay);
  }

  /**
   * Run a task repeatedly related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task. Return true to continue, false to stop.
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runEntityTaskTimer(Entity entity, BooleanSupplier runnable, long delay, long period) {
    return runEntityTaskTimer(entity, runnable, () -> {
    }, delay, period);
  }

  /**
   * Run a task repeatedly related to an entity
   *
   * @param entity   the entity that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  default Task runEntityTaskTimer(Entity entity, Runnable runnable, long delay, long period) {
    return runEntityTaskTimer(entity, runnable, () -> {
    }, delay, period);
  }

  /**
   * Run a task related to an entity with a finalizer.
   * The finalizer will be run both after the task is run and when the entity is retired.
   *
   * @param entity    the entity that the task is related to
   * @param runnable  the task
   * @param finalizer the finalizer
   *
   * @return the task
   */
  default Task runEntityTaskWithFinalizer(Entity entity, Runnable runnable, Runnable finalizer) {
    return runEntityTask(entity, () -> {
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
   * @param entity    the entity that the task is related to
   * @param runnable  the task
   * @param finalizer the finalizer
   * @param delay     the delay in ticks before the task is run
   *
   * @return the task
   */
  default Task runEntityTaskLaterWithFinalizer(Entity entity, Runnable runnable, Runnable finalizer, long delay) {
    return runEntityTaskLater(entity, () -> {
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
   * @param location the location that the task is related to
   * @param runnable the task
   *
   * @return the task
   */
  Task runLocationTask(Location location, Runnable runnable);

  /**
   * Run a delayed task related to a location
   *
   * @param location the location that the task is related to
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   *
   * @return the task
   */
  Task runLocationTaskLater(Location location, Runnable runnable, long delay);

  /**
   * Run a task repeatedly related to a location
   *
   * @param location the location that the task is related to
   * @param runnable the task. Return true to continue, false to stop.
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  Task runLocationTaskTimer(Location location, BooleanSupplier runnable, long delay, long period);

  /**
   * Run a task repeatedly related to a location
   *
   * @param location the location that the task is related to
   * @param runnable the task
   * @param delay    the delay in ticks before the task is run
   * @param period   the period in ticks between each run
   *
   * @return the task
   */
  default Task runLocationTaskTimer(Location location, Runnable runnable, long delay, long period) {
    return runLocationTaskTimer(location, () -> {
      runnable.run();
      return true;
    }, delay, period);
  }
}
