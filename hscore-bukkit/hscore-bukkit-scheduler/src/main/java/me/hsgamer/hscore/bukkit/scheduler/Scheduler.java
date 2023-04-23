package me.hsgamer.hscore.bukkit.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The scheduler
 */
public interface Scheduler {
  /**
   * Get the {@link Scheduler} for the given {@link Plugin}
   *
   * @param plugin the plugin
   *
   * @return the scheduler
   */
  static Scheduler plugin(Plugin plugin) {
    return SchedulerPool.plugin(plugin);
  }

  /**
   * Get the {@link Scheduler} for the given {@link JavaPlugin}
   *
   * @param clazz the plugin class
   * @param <T>   the plugin type
   *
   * @return the scheduler
   */
  static <T extends JavaPlugin> Scheduler plugin(Class<T> clazz) {
    return plugin(JavaPlugin.getPlugin(clazz));
  }

  /**
   * Get the {@link Scheduler} for the {@link JavaPlugin} that provides the class
   *
   * @param clazz the class
   *
   * @return the scheduler
   */
  static Scheduler providingPlugin(Class<?> clazz) {
    return plugin(JavaPlugin.getProvidingPlugin(clazz));
  }

  /**
   * Get the {@link Scheduler} for the given {@link JavaPlugin} that provides the {@link Scheduler} class
   *
   * @return the scheduler
   */
  static Scheduler current() {
    return providingPlugin(Scheduler.class);
  }

  /**
   * Cancel all tasks
   */
  void cancelAllTasks();

  /**
   * Get the {@link Runner} for asynchronous tasks
   *
   * @return the {@link Runner}
   */
  Runner async();

  /**
   * Get the {@link Runner} for synchronous tasks
   *
   * @return the {@link Runner}
   */
  Runner sync();

  /**
   * Get the {@link Runner} for the given type
   *
   * @param async the type
   *
   * @return the {@link Runner}
   */
  default Runner runner(boolean async) {
    return async ? async() : sync();
  }
}
