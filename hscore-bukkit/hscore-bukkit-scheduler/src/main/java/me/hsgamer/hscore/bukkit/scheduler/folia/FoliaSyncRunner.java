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

import static me.hsgamer.hscore.bukkit.scheduler.folia.FoliaScheduler.*;

class FoliaSyncRunner implements Runner {
  private final FoliaScheduler scheduler;

  FoliaSyncRunner(FoliaScheduler scheduler) {
    this.scheduler = scheduler;
  }

  private void addTask(ScheduledTask task) {
    scheduler.addTask(task);
  }

  @Override
  public Task runTask(Runnable runnable) {
    ScheduledTask task = Bukkit.getGlobalRegionScheduler().run(scheduler.getPlugin(), wrapRunnable(runnable));
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runTaskLater(Runnable runnable, TaskTime delay) {
    ScheduledTask task = Bukkit.getGlobalRegionScheduler().runDelayed(scheduler.getPlugin(), wrapRunnable(runnable), delay.getNormalizedTicks());
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runTaskTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
    ScheduledTask task = Bukkit.getGlobalRegionScheduler().runAtFixedRate(scheduler.getPlugin(), wrapRunnable(runnable), timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks());
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runEntityTask(Entity entity, Runnable runnable, Runnable retired) {
    if (!isEntityValid(entity)) {
      return runTask(retired);
    }
    ScheduledTask task = entity.getScheduler().run(scheduler.getPlugin(), wrapRunnable(runnable), retired);
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runEntityTaskLater(Entity entity, Runnable runnable, Runnable retired, TaskTime delay) {
    if (!isEntityValid(entity)) {
      return runTaskLater(retired, delay);
    }
    ScheduledTask task = entity.getScheduler().runDelayed(scheduler.getPlugin(), wrapRunnable(runnable), retired, delay.getNormalizedTicks());
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runEntityTaskTimer(Entity entity, BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime) {
    if (!isEntityValid(entity)) {
      return runTaskLater(retired, TaskTime.of(timerTaskTime.getDelayTicks()));
    }
    ScheduledTask task = entity.getScheduler().runAtFixedRate(scheduler.getPlugin(), wrapRunnable(runnable), retired, timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks());
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runLocationTask(Location location, Runnable runnable) {
    ScheduledTask task = Bukkit.getRegionScheduler().run(scheduler.getPlugin(), location, wrapRunnable(runnable));
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runLocationTaskLater(Location location, Runnable runnable, TaskTime delay) {
    ScheduledTask task = Bukkit.getRegionScheduler().runDelayed(scheduler.getPlugin(), location, wrapRunnable(runnable), delay.getNormalizedTicks());
    addTask(task);
    return wrapTask(task, false);
  }

  @Override
  public Task runLocationTaskTimer(Location location, BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
    ScheduledTask task = Bukkit.getRegionScheduler().runAtFixedRate(scheduler.getPlugin(), location, wrapRunnable(runnable), timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks());
    addTask(task);
    return wrapTask(task, false);
  }
}
