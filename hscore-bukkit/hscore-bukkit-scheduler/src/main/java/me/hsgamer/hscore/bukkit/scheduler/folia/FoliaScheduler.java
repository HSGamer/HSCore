package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * The Folia implementation of {@link Scheduler}
 */
public class FoliaScheduler implements Scheduler {
  private final Map<Plugin, Set<ScheduledTask>> pluginTaskMap = new ConcurrentHashMap<>();

  private void addTask(Plugin plugin, ScheduledTask scheduledTask) {
    pluginTaskMap.computeIfAbsent(plugin, p -> ConcurrentHashMap.newKeySet()).add(scheduledTask);
  }

  private long normalizeTick(long tick) {
    return Math.max(1, tick);
  }

  private long toMilliSecond(long tick) {
    return normalizeTick(tick) * 50;
  }

  private Consumer<ScheduledTask> wrapRunnable(BooleanSupplier runnable) {
    return scheduledTask -> {
      if (!runnable.getAsBoolean()) {
        scheduledTask.cancel();
      }
    };
  }

  private Task wrapTask(ScheduledTask scheduledTask, boolean async) {
    return new Task() {
      @Override
      public boolean isCancelled() {
        return scheduledTask.isCancelled();
      }

      @Override
      public void cancel() {
        scheduledTask.cancel();
      }

      @Override
      public boolean isAsync() {
        return async;
      }

      @Override
      public boolean isRepeating() {
        return scheduledTask.isRepeatingTask();
      }
    };
  }

  private boolean isEntityValid(Entity entity) {
    if (entity == null) {
      return false;
    }

    if (entity instanceof Player) {
      return ((Player) entity).isOnline();
    }

    return entity.isValid();
  }

  private Consumer<ScheduledTask> wrapRunnable(Entity entity, BooleanSupplier runnable, Runnable retired) {
    return scheduledTask -> {
      if (isEntityValid(entity)) {
        if (!runnable.getAsBoolean()) {
          scheduledTask.cancel();
        }
      } else {
        retired.run();
        scheduledTask.cancel();
      }
    };
  }

  private Consumer<ScheduledTask> wrapRunnable(Entity entity, Runnable runnable, Runnable retired) {
    return wrapRunnable(entity, () -> {
      runnable.run();
      return true;
    }, retired);
  }

  @Override
  public void cancelAllTasks(Plugin plugin) {
    Set<ScheduledTask> scheduledTasks = pluginTaskMap.remove(plugin);
    if (scheduledTasks != null) {
      scheduledTasks.forEach(scheduledTask -> {
        if (!scheduledTask.isCancelled()) {
          scheduledTask.cancel();
        }
      });
      scheduledTasks.clear();
    }

    Bukkit.getAsyncScheduler().cancelTasks(plugin);
    Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
  }

  @Override
  public Task runTask(Plugin plugin, Runnable runnable, boolean async) {
    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runNow(plugin, s -> runnable.run());
    } else {
      scheduledTask = Bukkit.getGlobalRegionScheduler().run(plugin, s -> runnable.run());
    }
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, async);
  }

  @Override
  public Task runTaskLater(Plugin plugin, Runnable runnable, long delay, boolean async) {
    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runDelayed(plugin, s -> runnable.run(), toMilliSecond(delay), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, s -> runnable.run(), normalizeTick(delay));
    }
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, async);
  }

  @Override
  public Task runTaskTimer(Plugin plugin, BooleanSupplier runnable, long delay, long period, boolean async) {
    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), normalizeTick(delay), normalizeTick(period));
    }
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, async);
  }

  @Override
  public Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, boolean async) {
    if (!isEntityValid(entity)) {
      return runTask(plugin, retired, async);
    }

    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runNow(plugin, wrapRunnable(entity, runnable, retired));
    } else {
      scheduledTask = entity.getScheduler().run(plugin, s -> runnable.run(), retired);
    }
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, async);
  }

  @Override
  public Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, boolean async) {
    if (!isEntityValid(entity)) {
      return runTaskLater(plugin, retired, delay, async);
    }

    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runDelayed(plugin, wrapRunnable(entity, runnable, retired), toMilliSecond(delay), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = entity.getScheduler().runDelayed(plugin, s -> runnable.run(), retired, normalizeTick(delay));
    }
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, async);
  }

  @Override
  public Task runEntityTaskTimer(Plugin plugin, Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period, boolean async) {
    if (!isEntityValid(entity)) {
      return runTaskLater(plugin, retired, delay, async);
    }

    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrapRunnable(entity, runnable, retired), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = entity.getScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), retired, normalizeTick(delay), normalizeTick(period));
    }
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, async);
  }

  @Override
  public Task runLocationTask(Plugin plugin, Location location, Runnable runnable) {
    ScheduledTask scheduledTask = Bukkit.getRegionScheduler().run(plugin, location, s -> runnable.run());
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, false);
  }

  @Override
  public Task runLocationTaskLater(Plugin plugin, Location location, Runnable runnable, long delay) {
    ScheduledTask scheduledTask = Bukkit.getRegionScheduler().runDelayed(plugin, location, s -> runnable.run(), normalizeTick(delay));
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, false);
  }

  @Override
  public Task runLocationTaskTimer(Plugin plugin, Location location, BooleanSupplier runnable, long delay, long period) {
    ScheduledTask scheduledTask = Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, wrapRunnable(runnable), normalizeTick(delay), normalizeTick(period));
    addTask(plugin, scheduledTask);
    return wrapTask(scheduledTask, false);
  }
}
