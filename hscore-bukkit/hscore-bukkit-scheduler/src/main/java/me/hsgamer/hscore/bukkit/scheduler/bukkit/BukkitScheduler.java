package me.hsgamer.hscore.bukkit.scheduler.bukkit;

import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BooleanSupplier;

/**
 * The Bukkit implementation of {@link Scheduler}
 */
public class BukkitScheduler implements Scheduler {
  private final BukkitSyncRunner syncRunner;
  private final BukkitAsyncRunner asyncRunner;
  private final Plugin plugin;

  public BukkitScheduler(Plugin plugin) {
    this.plugin = plugin;
    syncRunner = new BukkitSyncRunner(this);
    asyncRunner = new BukkitAsyncRunner(this);
  }

  static BukkitRunnable wrapRunnable(Runnable runnable) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        runnable.run();
      }
    };
  }

  static BukkitRunnable wrapRunnable(BooleanSupplier runnable) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        if (!runnable.getAsBoolean()) {
          cancel();
        }
      }
    };
  }

  static boolean isEntityValid(Entity entity) {
    if (entity == null) {
      return false;
    }

    if (entity instanceof Player) {
      return ((Player) entity).isOnline();
    }

    return entity.isValid();
  }

  static BukkitRunnable wrapRunnable(Entity entity, BooleanSupplier runnable, Runnable retired) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        if (isEntityValid(entity)) {
          if (!runnable.getAsBoolean()) {
            cancel();
          }
        } else {
          retired.run();
          cancel();
        }
      }
    };
  }

  static BukkitRunnable wrapRunnable(Entity entity, Runnable runnable, Runnable retired) {
    return wrapRunnable(entity, () -> {
      runnable.run();
      return true;
    }, retired);
  }

  static Task wrapTask(BukkitTask bukkitTask, boolean repeating) {
    return new Task() {
      @Override
      public boolean isCancelled() {
        try {
          return bukkitTask.isCancelled();
        } catch (Throwable throwable) {
          int taskId = bukkitTask.getTaskId();
          return !(Bukkit.getScheduler().isQueued(taskId) || Bukkit.getScheduler().isCurrentlyRunning(taskId));
        }
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

      @Override
      public Plugin getPlugin() {
        return bukkitTask.getOwner();
      }
    };
  }

  Plugin getPlugin() {
    return plugin;
  }

  @Override
  public void cancelAllTasks() {
    Bukkit.getScheduler().cancelTasks(plugin);
  }

  @Override
  public Runner async() {
    return asyncRunner;
  }

  @Override
  public Runner sync() {
    return syncRunner;
  }
}
