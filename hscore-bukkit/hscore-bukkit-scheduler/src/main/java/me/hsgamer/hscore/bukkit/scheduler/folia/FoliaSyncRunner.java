package me.hsgamer.hscore.bukkit.scheduler.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

import static me.hsgamer.hscore.bukkit.scheduler.folia.FoliaScheduler.*;

class FoliaSyncRunner implements Runner {
  private final FoliaScheduler scheduler;

  FoliaSyncRunner(FoliaScheduler scheduler) {
    this.scheduler = scheduler;
  }

  private void addTask(Plugin plugin, ScheduledTask task) {
    scheduler.addTask(plugin, task);
  }

  @Override
  public Task runTask(Plugin plugin, Runnable runnable) {
    ScheduledTask task = Bukkit.getGlobalRegionScheduler().run(plugin, wrapRunnable(runnable));
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runTaskLater(Plugin plugin, Runnable runnable, long delay) {
    ScheduledTask task = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, wrapRunnable(runnable), normalizeTick(delay));
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runTaskTimer(Plugin plugin, BooleanSupplier runnable, long delay, long period) {
    ScheduledTask task = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), normalizeTick(delay), normalizeTick(period));
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired) {
    if (!isEntityValid(entity)) {
      return runTask(plugin, retired);
    }
    ScheduledTask task = entity.getScheduler().run(plugin, wrapRunnable(runnable), retired);
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay) {
    if (!isEntityValid(entity)) {
      return runTaskLater(plugin, retired, delay);
    }
    ScheduledTask task = entity.getScheduler().runDelayed(plugin, wrapRunnable(runnable), retired, normalizeTick(delay));
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runEntityTaskTimer(Plugin plugin, Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period) {
    if (!isEntityValid(entity)) {
      return runTaskLater(plugin, retired, delay);
    }
    ScheduledTask task = entity.getScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), retired, normalizeTick(delay), normalizeTick(period));
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runLocationTask(Plugin plugin, Location location, Runnable runnable) {
    ScheduledTask task = Bukkit.getRegionScheduler().run(plugin, location, wrapRunnable(runnable));
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runLocationTaskLater(Plugin plugin, Location location, Runnable runnable, long delay) {
    ScheduledTask task = Bukkit.getRegionScheduler().runDelayed(plugin, location, wrapRunnable(runnable), normalizeTick(delay));
    addTask(plugin, task);
    return wrapTask(task, false);
  }

  @Override
  public Task runLocationTaskTimer(Plugin plugin, Location location, BooleanSupplier runnable, long delay, long period) {
    ScheduledTask task = Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, wrapRunnable(runnable), normalizeTick(delay), normalizeTick(period));
    addTask(plugin, task);
    return wrapTask(task, false);
  }
}
