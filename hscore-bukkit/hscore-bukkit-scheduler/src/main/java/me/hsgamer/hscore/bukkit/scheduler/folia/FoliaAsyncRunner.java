package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static me.hsgamer.hscore.bukkit.scheduler.folia.FoliaScheduler.*;

class FoliaAsyncRunner implements Runner {
  private final FoliaScheduler scheduler;

  FoliaAsyncRunner(FoliaScheduler scheduler) {
    this.scheduler = scheduler;
  }

  private void addTask(Plugin plugin, ScheduledTask task) {
    scheduler.addTask(plugin, task);
  }

  private Consumer<ScheduledTask> wrapEntityRunnable(Entity entity, BooleanSupplier runnable, Runnable retired) {
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

  private Consumer<ScheduledTask> wrapEntityRunnable(Entity entity, Runnable runnable, Runnable retired) {
    return wrapEntityRunnable(entity, () -> {
      runnable.run();
      return true;
    }, retired);
  }

  @Override
  public Task runTask(Plugin plugin, Runnable runnable) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runNow(plugin, wrapRunnable(runnable));
    addTask(plugin, task);
    return wrapTask(task, true);
  }

  @Override
  public Task runTaskLater(Plugin plugin, Runnable runnable, long delay) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runDelayed(plugin, wrapRunnable(runnable), toMilliSecond(delay), TimeUnit.MILLISECONDS);
    addTask(plugin, task);
    return wrapTask(task, true);
  }

  @Override
  public Task runTaskTimer(Plugin plugin, BooleanSupplier runnable, long delay, long period) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    addTask(plugin, task);
    return wrapTask(task, true);
  }

  @Override
  public Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runNow(plugin, wrapEntityRunnable(entity, runnable, retired));
    addTask(plugin, task);
    return wrapTask(task, true);
  }

  @Override
  public Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runDelayed(plugin, wrapEntityRunnable(entity, runnable, retired), toMilliSecond(delay), TimeUnit.MILLISECONDS);
    addTask(plugin, task);
    return wrapTask(task, true);
  }

  @Override
  public Task runEntityTaskTimer(Plugin plugin, Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrapEntityRunnable(entity, runnable, retired), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    addTask(plugin, task);
    return wrapTask(task, true);
  }

  @Override
  public Task runLocationTask(Plugin plugin, Location location, Runnable runnable) {
    return runTask(plugin, runnable);
  }

  @Override
  public Task runLocationTaskLater(Plugin plugin, Location location, Runnable runnable, long delay) {
    return runTaskLater(plugin, runnable, delay);
  }

  @Override
  public Task runLocationTaskTimer(Plugin plugin, Location location, BooleanSupplier runnable, long delay, long period) {
    return runTaskTimer(plugin, runnable, delay, period);
  }
}
