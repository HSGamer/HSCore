package me.hsgamer.hscore.bukkit.scheduler.bukkit;

import me.hsgamer.hscore.bukkit.scheduler.Runner;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

import static me.hsgamer.hscore.bukkit.scheduler.bukkit.BukkitScheduler.wrapRunnable;
import static me.hsgamer.hscore.bukkit.scheduler.bukkit.BukkitScheduler.wrapTask;

class BukkitSyncRunner implements Runner {
  BukkitSyncRunner() {
    // EMPTY
  }

  @Override
  public Task runTask(Plugin plugin, Runnable runnable) {
    return wrapTask(wrapRunnable(runnable).runTask(plugin), false);
  }

  @Override
  public Task runTaskLater(Plugin plugin, Runnable runnable, long delay) {
    return wrapTask(wrapRunnable(runnable).runTaskLater(plugin, delay), false);
  }

  @Override
  public Task runTaskTimer(Plugin plugin, BooleanSupplier runnable, long delay, long period) {
    return wrapTask(wrapRunnable(runnable).runTaskTimer(plugin, delay, period), false);
  }

  @Override
  public Task runEntityTask(Plugin plugin, Entity entity, Runnable runnable, Runnable retired) {
    return wrapTask(wrapRunnable(entity, runnable, retired).runTask(plugin), false);
  }

  @Override
  public Task runEntityTaskLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retired, long delay) {
    return wrapTask(wrapRunnable(entity, runnable, retired).runTaskLater(plugin, delay), false);
  }

  @Override
  public Task runEntityTaskTimer(Plugin plugin, Entity entity, BooleanSupplier runnable, Runnable retired, long delay, long period) {
    return wrapTask(wrapRunnable(entity, runnable, retired).runTaskTimer(plugin, delay, period), false);
  }

  @Override
  public Task runLocationTask(Plugin plugin, Location location, Runnable runnable) {
    return wrapTask(wrapRunnable(runnable).runTask(plugin), false);
  }

  @Override
  public Task runLocationTaskLater(Plugin plugin, Location location, Runnable runnable, long delay) {
    return wrapTask(wrapRunnable(runnable).runTaskLater(plugin, delay), false);
  }

  @Override
  public Task runLocationTaskTimer(Plugin plugin, Location location, BooleanSupplier runnable, long delay, long period) {
    return wrapTask(wrapRunnable(runnable).runTaskTimer(plugin, delay, period), false);
  }
}
