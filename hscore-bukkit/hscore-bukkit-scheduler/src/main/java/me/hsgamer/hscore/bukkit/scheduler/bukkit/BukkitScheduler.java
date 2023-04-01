package me.hsgamer.hscore.bukkit.scheduler.bukkit;

import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * The Bukkit implementation of {@link Scheduler}
 */
public class BukkitScheduler implements Scheduler {
  private Task wrapTask(BukkitTask bukkitTask, boolean repeating) {
    return new Task() {
      @Override
      public boolean isCancelled() {
        return bukkitTask.isCancelled();
      }

      @Override
      public void cancel() {
        bukkitTask.cancel();
      }

      @Override
      public boolean isAsync() {
        return !bukkitTask.isSync();
      }

      @Override
      public boolean isRepeating() {
        return repeating;
      }
    };
  }

  private BukkitRunnable wrapRunnable(Runnable runnable) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        runnable.run();
      }
    };
  }

  private BukkitRunnable wrapRunnable(Entity entity, Runnable runnable, Runnable retired) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        if (entity.isValid()) {
          runnable.run();
        } else {
          retired.run();
          cancel();
        }
      }
    };
  }

  private BukkitTask runTask(Plugin plugin, BukkitRunnable bukkitRunnable, boolean async) {
    if (async) {
      return bukkitRunnable.runTaskAsynchronously(plugin);
    } else {
      return bukkitRunnable.runTask(plugin);
    }
  }

  private BukkitTask runTaskLater(Plugin plugin, BukkitRunnable bukkitRunnable, long delay, boolean async) {
    if (async) {
      return bukkitRunnable.runTaskLaterAsynchronously(plugin, delay);
    } else {
      return bukkitRunnable.runTaskLater(plugin, delay);
    }
  }

  private BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable bukkitRunnable, long delay, long period, boolean async) {
    if (async) {
      return bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
    } else {
      return bukkitRunnable.runTaskTimer(plugin, delay, period);
    }
  }

  @Override
  public void cancelAllTasks(Plugin plugin) {
    Bukkit.getScheduler().cancelTasks(plugin);
  }

  @Override
  public Task runTask(Plugin plugin, Runnable runnable, boolean async) {
    return wrapTask(runTask(plugin, wrapRunnable(runnable), async), false);
  }

  @Override
  public Task runTaskLater(Plugin plugin, Runnable runnable, long delay, boolean async) {
    return wrapTask(runTaskLater(plugin, wrapRunnable(runnable), delay, async), false);
  }

  @Override
  public Task runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period, boolean async) {
    return wrapTask(runTaskTimer(plugin, wrapRunnable(runnable), delay, period, async), true);
  }

  @Override
  public Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, boolean async) {
    return wrapTask(runTask(plugin, wrapRunnable(entity, runnable, retired), async), false);
  }

  @Override
  public Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, boolean async) {
    return wrapTask(runTaskLater(plugin, wrapRunnable(entity, runnable, retired), delay, async), false);
  }

  @Override
  public Task runEntityTaskTimer(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay, long period, boolean async) {
    return wrapTask(runTaskTimer(plugin, wrapRunnable(entity, runnable, retired), delay, period, async), true);
  }
}
