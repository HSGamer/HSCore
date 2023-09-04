package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import me.hsgamer.hscore.bukkit.scheduler.TaskTime;
import me.hsgamer.hscore.bukkit.scheduler.TimerTaskTime;
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

  private Task scheduleTask(Consumer<ScheduledTask> consumer) {
    ScheduledTask task = Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), consumer);
    addTask(task);
    return wrapTask(task, true);
  }

  private Task scheduleTaskLater(Consumer<ScheduledTask> consumer, TaskTime delay) {
    long time = delay.getTime();
    ScheduledTask task;
    if (time > 0) {
      task = Bukkit.getAsyncScheduler().runDelayed(scheduler.getPlugin(), consumer, time, delay.getUnit());
    } else {
      task = Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), consumer);
    }
    addTask(task);
    return wrapTask(task, true);
  }

  private Task scheduleTaskTimer(Consumer<ScheduledTask> consumer, TimerTaskTime timerTaskTime) {
    long delay = timerTaskTime.getDelay();
    long period = timerTaskTime.getNormalizedPeriod();
    ScheduledTask task;
    if (delay > 0) {
      task = Bukkit.getAsyncScheduler().runAtFixedRate(scheduler.getPlugin(), consumer, delay, period, timerTaskTime.getUnit());
    } else {
      task = Bukkit.getAsyncScheduler().runAtFixedRate(scheduler.getPlugin(), consumer, 1, timerTaskTime.getUnit().toMillis(period), TimeUnit.MILLISECONDS);
    }
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runTask(Runnable runnable) {
    return scheduleTask(wrapRunnable(runnable));
  }

  @Override
  public Task runTaskLater(Runnable runnable, TaskTime delay) {
    return scheduleTaskLater(wrapRunnable(runnable), delay);
  }

  @Override
  public Task runTaskTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
    return scheduleTaskTimer(wrapRunnable(runnable), timerTaskTime);
  }

  @Override
  public Task runEntityTask(Entity entity, Runnable runnable, Runnable retired) {
    return scheduleTask(wrapEntityRunnable(entity, runnable, retired));
  }

  @Override
  public Task runEntityTaskLater(Entity entity, Runnable runnable, Runnable retired, TaskTime delay) {
    return scheduleTaskLater(wrapEntityRunnable(entity, runnable, retired), delay);
  }

  @Override
  public Task runEntityTaskTimer(Entity entity, BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime) {
    return scheduleTaskTimer(wrapEntityRunnable(entity, runnable, retired), timerTaskTime);
  }

  @Override
  public Task runLocationTask(Location location, Runnable runnable) {
    return runTask(runnable);
  }

  @Override
  public Task runLocationTaskLater(Location location, Runnable runnable, TaskTime delay) {
    return runTaskLater(runnable, delay);
  }

  @Override
  public Task runLocationTaskTimer(Location location, BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
    return runTaskTimer(runnable, timerTaskTime);
  }
}
