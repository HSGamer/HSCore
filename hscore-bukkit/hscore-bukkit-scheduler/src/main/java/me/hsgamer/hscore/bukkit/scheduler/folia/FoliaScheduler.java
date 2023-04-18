package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * The Folia implementation of {@link Scheduler}
 */
public class FoliaScheduler implements Scheduler {
  private final Map<Plugin, Set<ScheduledTask>> pluginTaskMap = new ConcurrentHashMap<>();
  private final FoliaSyncRunner syncRunner = new FoliaSyncRunner(this);
  private final FoliaAsyncRunner asyncRunner = new FoliaAsyncRunner(this);

  static long normalizeTick(long tick) {
    return Math.max(1, tick);
  }

  static long toMilliSecond(long tick) {
    return normalizeTick(tick) * 50;
  }

  static Consumer<ScheduledTask> wrapRunnable(BooleanSupplier runnable) {
    return scheduledTask -> {
      if (!runnable.getAsBoolean()) {
        scheduledTask.cancel();
      }
    };
  }

  static Consumer<ScheduledTask> wrapRunnable(Runnable runnable) {
    return scheduledTask -> runnable.run();
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

  void addTask(Plugin plugin, ScheduledTask scheduledTask) {
    pluginTaskMap.computeIfAbsent(plugin, p -> ConcurrentHashMap.newKeySet()).add(scheduledTask);
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
  public Runner async() {
    return asyncRunner;
  }

  @Override
  public Runner sync() {
    return syncRunner;
  }
}
