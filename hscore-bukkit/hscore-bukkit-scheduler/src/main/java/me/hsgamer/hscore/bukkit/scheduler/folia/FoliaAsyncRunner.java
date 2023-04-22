package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static me.hsgamer.hscore.bukkit.scheduler.folia.FoliaScheduler.*;

class FoliaAsyncRunner implements Runner {
  private final FoliaScheduler scheduler;

  FoliaAsyncRunner(FoliaScheduler scheduler) {
    this.scheduler = scheduler;
  }

  private void addTask(ScheduledTask task) {
    scheduler.addTask(task);
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
  public Task runTask(Runnable runnable) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), wrapRunnable(runnable));
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runTaskLater(Runnable runnable, long delay) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runDelayed(scheduler.getPlugin(), wrapRunnable(runnable), toMilliSecond(delay), TimeUnit.MILLISECONDS);
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runTaskTimer(BooleanSupplier runnable, long delay, long period) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runAtFixedRate(scheduler.getPlugin(), wrapRunnable(runnable), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runEntityTask(Entity entity, Runnable runnable, Runnable retired) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), wrapEntityRunnable(entity, runnable, retired));
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runEntityTaskLater(Entity entity, Runnable runnable, Runnable retired, long delay) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runDelayed(scheduler.getPlugin(), wrapEntityRunnable(entity, runnable, retired), toMilliSecond(delay), TimeUnit.MILLISECONDS);
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runEntityTaskTimer(Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runAtFixedRate(scheduler.getPlugin(), wrapEntityRunnable(entity, runnable, retired), toMilliSecond(delay), toMilliSecond(period), TimeUnit.MILLISECONDS);
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runLocationTask(Location location, Runnable runnable) {
    return runTask(runnable);
  }

  @Override
  public Task runLocationTaskLater(Location location, Runnable runnable, long delay) {
    return runTaskLater(runnable, delay);
  }

  @Override
  public Task runLocationTaskTimer(Location location, BooleanSupplier runnable, long delay, long period) {
    return runTaskTimer(runnable, delay, period);
  }
}
