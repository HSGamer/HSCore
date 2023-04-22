package me.hsgamer.hscore.bukkit.scheduler.bukkit;

import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.BooleanSupplier;

import static me.hsgamer.hscore.bukkit.scheduler.bukkit.BukkitScheduler.wrapRunnable;
import static me.hsgamer.hscore.bukkit.scheduler.bukkit.BukkitScheduler.wrapTask;

class BukkitAsyncRunner implements Runner {
  private final BukkitScheduler scheduler;

  BukkitAsyncRunner(BukkitScheduler scheduler) {
    this.scheduler = scheduler;
  }

  @Override
  public Task runTask(Runnable runnable) {
    return wrapTask(wrapRunnable(runnable).runTaskAsynchronously(scheduler.getPlugin()), false);
  }

  @Override
  public Task runTaskLater(Runnable runnable, long delay) {
    return wrapTask(wrapRunnable(runnable).runTaskLaterAsynchronously(scheduler.getPlugin(), delay), false);
  }

  @Override
  public Task runTaskTimer(BooleanSupplier runnable, long delay, long period) {
    return wrapTask(wrapRunnable(runnable).runTaskTimerAsynchronously(scheduler.getPlugin(), delay, period), true);
  }

  @Override
  public Task runEntityTask(Entity entity, Runnable runnable, Runnable retired) {
    return wrapTask(wrapRunnable(entity, runnable, retired).runTaskAsynchronously(scheduler.getPlugin()), false);
  }

  @Override
  public Task runEntityTaskLater(Entity entity, Runnable runnable, Runnable retired, long delay) {
    return wrapTask(wrapRunnable(entity, runnable, retired).runTaskLaterAsynchronously(scheduler.getPlugin(), delay), false);
  }

  @Override
  public Task runEntityTaskTimer(Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period) {
    return wrapTask(wrapRunnable(entity, runnable, retired).runTaskTimerAsynchronously(scheduler.getPlugin(), delay, period), true);
  }

  @Override
  public Task runLocationTask(Location location, Runnable runnable) {
    return wrapTask(wrapRunnable(runnable).runTaskAsynchronously(scheduler.getPlugin()), false);
  }

  @Override
  public Task runLocationTaskLater(Location location, Runnable runnable, long delay) {
    return wrapTask(wrapRunnable(runnable).runTaskLaterAsynchronously(scheduler.getPlugin(), delay), false);
  }

  @Override
  public Task runLocationTaskTimer(Location location, BooleanSupplier runnable, long delay, long period) {
    return wrapTask(wrapRunnable(runnable).runTaskTimerAsynchronously(scheduler.getPlugin(), delay, period), true);
  }
}
