package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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

  private Consumer<ScheduledTask> wrap(BooleanSupplier runnable) {
    return scheduledTask -> {
      if (!runnable.getAsBoolean()) {
        scheduledTask.cancel();
      }
    };
  }

  private Task wrap(ScheduledTask scheduledTask, boolean async) {
    return new Task() {
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

  private Consumer<ScheduledTask> wrap(Entity entity, Runnable runnable, Runnable retired) {
    return scheduledTask -> {
      if (entity.isValid()) {
        runnable.run();
      } else {
        retired.run();
        scheduledTask.cancel();
      }
    };
  }

  private Consumer<ScheduledTask> wrap(Entity entity, BooleanSupplier runnable, Runnable retired) {
    return scheduledTask -> {
      if (entity.isValid()) {
        if (!runnable.getAsBoolean()) {
          scheduledTask.cancel();
        }
      } else {
        retired.run();
        scheduledTask.cancel();
      }
    };
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
    return wrap(scheduledTask, async);
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
    return wrap(scheduledTask, async);
  }

  @Override
  public Task runTaskTimer(Plugin plugin, BooleanSupplier runnable, long delay, long period, boolean async) {
    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrap(runnable), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, wrap(runnable), normalizeTick(delay), normalizeTick(period));
    }
    addTask(plugin, scheduledTask);
    return wrap(scheduledTask, async);
  }

  @Override
  public Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, boolean async) {
    if (entity == null || !entity.isValid()) {
      return Task.completed(async, false);
    }

    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runNow(plugin, wrap(entity, runnable, retired));
    } else {
      scheduledTask = entity.getScheduler().run(plugin, s -> runnable.run(), retired);
    }
    addTask(plugin, scheduledTask);
    return wrap(scheduledTask, async);
  }

  @Override
  public Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, boolean async) {
    if (entity == null || !entity.isValid()) {
      return Task.completed(async, false);
    }

    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runDelayed(plugin, wrap(entity, runnable, retired), toMilliSecond(delay), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = entity.getScheduler().runDelayed(plugin, s -> runnable.run(), retired, normalizeTick(delay));
    }
    addTask(plugin, scheduledTask);
    return wrap(scheduledTask, async);
  }

  @Override
  public Task runEntityTaskTimer(Plugin plugin, Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period, boolean async) {
    if (entity == null || !entity.isValid()) {
      return Task.completed(async, true);
    }

    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrap(entity, runnable, retired), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = entity.getScheduler().runAtFixedRate(plugin, wrap(runnable), retired, normalizeTick(delay), normalizeTick(period));
    }
    addTask(plugin, scheduledTask);
    return wrap(scheduledTask, async);
  }
}
