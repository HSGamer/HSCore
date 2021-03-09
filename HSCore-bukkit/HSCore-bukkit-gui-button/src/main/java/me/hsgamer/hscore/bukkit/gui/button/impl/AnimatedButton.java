package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.ui.property.Updatable;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The animated button with child buttons as frames
 */
public class AnimatedButton implements Button, Updatable {
  private final List<Button> buttons = new ArrayList<>();
  private final long period;
  private final boolean async;
  private final Plugin plugin;
  private int currentIndex = 0;
  private BukkitTask task;

  /**
   * Create a new button
   *
   * @param plugin the plugin
   * @param period the period between 2 child buttons
   * @param async  should the update task be asynchronous ?
   */
  public AnimatedButton(Plugin plugin, long period, boolean async) {
    this.period = period;
    this.plugin = plugin;
    this.async = async;
  }

  /**
   * Create a new button
   *
   * @param plugin      the plugin
   * @param period      the period between 2 child buttons
   * @param async       should the update task be asynchronous ?
   * @param childButton the child button (or frame)
   */
  public AnimatedButton(Plugin plugin, long period, boolean async, Button... childButton) {
    this(plugin, period, async);
    addChildButtons(childButton);
  }

  /**
   * Add child buttons
   *
   * @param childButton the child button (or frame)
   */
  public void addChildButtons(Button... childButton) {
    this.buttons.addAll(Arrays.asList(childButton));
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    return this.buttons.get(currentIndex).getItemStack(uuid);
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    this.buttons.get(currentIndex).handleAction(uuid, event);
  }

  @Override
  public void init() {
    if (this.buttons.isEmpty()) {
      throw new IllegalArgumentException("There is no child button for this animated button");
    }
    this.buttons.forEach(Button::init);
    if (async) {
      this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, this::update, 0, this.period);
    } else {
      this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, this::update, 0, this.period);
    }
  }

  @Override
  public void stop() {
    if (!this.task.isCancelled()) {
      this.task.cancel();
    }
    this.buttons.forEach(Button::stop);
  }

  @Override
  public void update() {
    this.currentIndex = (this.currentIndex + 1) % this.buttons.size();
  }

  /**
   * Get the list of buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return buttons;
  }
}
