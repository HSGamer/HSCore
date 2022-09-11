package me.hsgamer.hscore.bukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * The base listener
 *
 * @param <P> the plugin
 */
public abstract class BaseListener<P extends Plugin> implements Listener {
  /**
   * The plugin
   */
  protected final P plugin;

  /**
   * Create a new listener
   *
   * @param plugin the plugin
   */
  protected BaseListener(P plugin) {
    this.plugin = plugin;
  }

  /**
   * Register the listener
   */
  public final void setup() {
    getExecutors().forEach(executor -> executor.register(plugin, this));
  }

  /**
   * Unregister the listener
   */
  public final void cleanup() {
    HandlerList.unregisterAll(this);
  }

  /**
   * Get the executors
   *
   * @return the executors
   */
  public abstract List<ListenerExecutor<? extends Event>> getExecutors();
}
