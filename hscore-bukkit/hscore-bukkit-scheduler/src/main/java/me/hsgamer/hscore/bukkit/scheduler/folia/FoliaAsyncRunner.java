package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import me.hsgamer.hscore.bukkit.scheduler.TaskTime;
import me.hsgamer.hscore.bukkit.scheduler.TimerTaskTime;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

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
    return new Consumer<ScheduledTask>() {
      @Override
      public void accept(ScheduledTask scheduledTask) {
        synchronized (this) {
          if (isEntityValid(entity)) {
            if (!runnable.getAsBoolean()) {
              scheduledTask.cancel();
            }
          } else {
            retired.run();
            scheduledTask.cancel();
          }
        }
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
  public Task runTaskLater(Runnable runnable, TaskTime delay) {
    long time = delay.getTime();
    ScheduledTask task;
    if (time > 0) {
      task = Bukkit.getAsyncScheduler().runDelayed(scheduler.getPlugin(), wrapRunnable(runnable), time, delay.getUnit());
    } else {
      task = Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), wrapRunnable(runnable));
    }
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runTaskTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
    long delay = timerTaskTime.getDelay();
    long period = timerTaskTime.getNormalizedPeriod();
    Consumer<ScheduledTask> wrappedRunnable = wrapRunnable(runnable);

    if (delay <= 0) {
      addTask(Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), wrappedRunnable));
    }

    ScheduledTask task = Bukkit.getAsyncScheduler().runAtFixedRate(scheduler.getPlugin(), wrappedRunnable, delay <= 0 ? period : delay, period, timerTaskTime.getUnit());
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
  public Task runEntityTaskLater(Entity entity, Runnable runnable, Runnable retired, TaskTime delay) {
    long time = delay.getTime();
    ScheduledTask task;
    if (time > 0) {
      task = Bukkit.getAsyncScheduler().runDelayed(scheduler.getPlugin(), wrapEntityRunnable(entity, runnable, retired), time, delay.getUnit());
    } else {
      task = Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), wrapEntityRunnable(entity, runnable, retired));
    }
    addTask(task);
    return wrapTask(task, true);
  }

  @Override
  public Task runEntityTaskTimer(Entity entity, BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime) {
    long delay = timerTaskTime.getDelay();
    long period = timerTaskTime.getNormalizedPeriod();
    Consumer<ScheduledTask> wrappedRunnable = wrapEntityRunnable(entity, runnable, retired);

    if (delay <= 0) {
      addTask(Bukkit.getAsyncScheduler().runNow(scheduler.getPlugin(), wrappedRunnable));
    }

    ScheduledTask task = Bukkit.getAsyncScheduler().runAtFixedRate(scheduler.getPlugin(), wrappedRunnable, delay <= 0 ? period : delay, period, timerTaskTime.getUnit());
    addTask(task);

    return wrapTask(task, true);
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
