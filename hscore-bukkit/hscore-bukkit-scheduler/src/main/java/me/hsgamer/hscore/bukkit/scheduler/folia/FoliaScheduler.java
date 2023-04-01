package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * The Folia implementation of {@link Scheduler}
 */
public class FoliaScheduler implements Scheduler {
  private long normalizeTick(long tick) {
    return Math.max(1, tick);
  }

  private long toMilliSecond(long tick) {
    return normalizeTick(tick) * 50;
  }

  @Override
  public void cancelAllTasks(Plugin plugin) {
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
    return wrap(scheduledTask, async);
  }

  @Override
  public Task runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period, boolean async) {
    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, s -> runnable.run(), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, s -> runnable.run(), normalizeTick(delay), normalizeTick(period));
    }
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
    return wrap(scheduledTask, async);
  }

  @Override
  public Task runEntityTaskTimer(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, long period, boolean async) {
    if (entity == null || !entity.isValid()) {
      return Task.completed(async, true);
    }

    ScheduledTask scheduledTask;
    if (async) {
      scheduledTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrap(entity, runnable, retired), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    } else {
      scheduledTask = entity.getScheduler().runAtFixedRate(plugin, s -> runnable.run(), retired, normalizeTick(delay), normalizeTick(period));
    }
    return wrap(scheduledTask, async);
  }

  private Task wrap(ScheduledTask scheduledTask, boolean async) {
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
}
