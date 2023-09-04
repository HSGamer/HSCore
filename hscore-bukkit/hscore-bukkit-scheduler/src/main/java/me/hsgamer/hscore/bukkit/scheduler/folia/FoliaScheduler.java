package me.hsgamer.hscore.bukkit.scheduler.folia;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * The Folia implementation of {@link Scheduler}
 */
public class FoliaScheduler implements Scheduler {
  private final Cache<ScheduledTask, Boolean> tasks = CacheBuilder.newBuilder().weakKeys().build();
  private final FoliaSyncRunner syncRunner;
  private final FoliaAsyncRunner asyncRunner;
  private final Plugin plugin;

  public FoliaScheduler(Plugin plugin) {
    this.plugin = plugin;
    this.syncRunner = new FoliaSyncRunner(this);
    this.asyncRunner = new FoliaAsyncRunner(this);
  }

  static Consumer<ScheduledTask> wrapRunnable(BooleanSupplier runnable) {
    return new Consumer<ScheduledTask>() {
      @Override
      public void accept(ScheduledTask scheduledTask) {
        synchronized (this) {
          if (!runnable.getAsBoolean()) {
            scheduledTask.cancel();
          }
        }
      }
    };
  }

  static Consumer<ScheduledTask> wrapRunnable(Runnable runnable) {
    return new Consumer<ScheduledTask>() {
      @Override
      public void accept(ScheduledTask scheduledTask) {
        synchronized (this) {
          runnable.run();
        }
      }
    };
  }

  static Task wrapTask(ScheduledTask scheduledTask, boolean async) {
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

      @Override
      public Plugin getPlugin() {
        return scheduledTask.getOwningPlugin();
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

  Plugin getPlugin() {
    return plugin;
  }

  void addTask(ScheduledTask scheduledTask) {
    tasks.put(scheduledTask, true);
  }

  @Override
  public void cancelAllTasks() {
    tasks.asMap().keySet().forEach(ScheduledTask::cancel);
    tasks.invalidateAll();

    Bukkit.getAsyncScheduler().cancelTasks(plugin);
    Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
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
