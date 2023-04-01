package me.hsgamer.hscore.bukkit.simpleplugin;

import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A convenient {@link JavaPlugin} implementation
 */
public class SimplePlugin extends JavaPlugin {
  private final List<Runnable> disableFunctions = new ArrayList<>();

  public SimplePlugin() {
    super();
    preLoad();
  }

  @Override
  public void onLoad() {
    load();
  }

  @Override
  public void onEnable() {
    enable();

    Scheduler.CURRENT.runTask(this, this::postEnable, false);
  }

  @Override
  public void onDisable() {
    disable();

    disableFunctions.forEach(Runnable::run);
    disableFunctions.clear();

    Scheduler.CURRENT.cancelAllTasks(this);
    getServer().getServicesManager().unregisterAll(this);
    HandlerList.unregisterAll(this);

    postDisable();
  }

  /**
   * Execute on the constructor
   */
  public void preLoad() {
    // Preload logic
  }

  /**
   * Execute when loading the plugin
   */
  public void load() {
    // Load logic
  }

  /**
   * Execute when enabling the plugin
   */
  public void enable() {
    // Enable logic
  }

  /**
   * Execute after enabling all plugins
   */
  public void postEnable() {
    // Post Enable logic
  }

  /**
   * Execute when disabling the plugin
   */
  public void disable() {
    // Disable logic
  }

  /**
   * Execute after disabling all tasks, listeners and commands
   */
  public void postDisable() {
    // Post Disable logic
  }

  /**
   * Register the listener
   *
   * @param listener the listener
   */
  public void registerListener(Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }

  /**
   * Register a provider for the service
   *
   * @param service  the service
   * @param provider the provider
   * @param priority the priority of the provider
   * @param <T>      the type of the service
   */
  public <T> void registerProvider(Class<T> service, T provider, ServicePriority priority) {
    getServer().getServicesManager().register(service, provider, this, priority);
  }

  /**
   * Register a provider for the service in the normal priority
   *
   * @param service  the service
   * @param provider the provider
   * @param <T>      the type of the service
   */
  public <T> void registerProvider(Class<T> service, T provider) {
    registerProvider(service, provider, ServicePriority.Normal);
  }

  /**
   * Load the provider of the service
   *
   * @param service the service
   * @param <T>     the type of the service
   *
   * @return the provider
   */
  public <T> T loadProvider(Class<T> service) {
    return getServer().getServicesManager().load(service);
  }

  /**
   * Get the registered provider of the service
   *
   * @param service the service
   * @param <T>     the type of the service
   *
   * @return the provider
   */
  public <T> RegisteredServiceProvider<T> getProvider(Class<T> service) {
    return getServer().getServicesManager().getRegistration(service);
  }

  /**
   * Get all providers of the service
   *
   * @param service the service
   * @param <T>     the type of the service
   *
   * @return the collection of providers
   */
  public <T> Collection<RegisteredServiceProvider<T>> getAllProviders(Class<T> service) {
    return getServer().getServicesManager().getRegistrations(service);
  }

  /**
   * Add a function that will be called when the plugin is disabled.
   * When the plugin is disabled, the added functions will be called and then removed from the list.
   *
   * @param disableFunction the disable function
   */
  public void addDisableFunction(Runnable disableFunction) {
    disableFunctions.add(disableFunction);
  }
}
