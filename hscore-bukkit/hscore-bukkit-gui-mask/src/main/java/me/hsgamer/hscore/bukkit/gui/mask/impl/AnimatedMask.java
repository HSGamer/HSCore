package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;
import me.hsgamer.hscore.ui.property.Updatable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * The animated mask with child masks as frames
 */
public class AnimatedMask extends BaseMask implements Updatable {
  private final List<Mask> masks = new ArrayList<>();
  private final long period;
  private final boolean async;
  private final Plugin plugin;
  private int currentIndex = 0;
  private BukkitTask task;

  /**
   * Create a new mask
   *
   * @param name      the name of the mask
   * @param plugin    the plugin
   * @param period    the period between 2 child buttons
   * @param async     should the update task be asynchronous ?
   * @param childMask the child mask (or frame)
   */
  public AnimatedMask(String name, Plugin plugin, long period, boolean async, Mask... childMask) {
    super(name);
    this.period = period;
    this.plugin = plugin;
    this.async = async;
    addChildMasks(childMask);
  }

  /**
   * Add child masks
   *
   * @param childMask the child mask (or frame)
   */
  public void addChildMasks(Mask... childMask) {
    this.masks.addAll(Arrays.asList(childMask));
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return this.masks.get(currentIndex).generateButtons(uuid);
  }

  @Override
  public void init() {
    if (this.masks.isEmpty()) {
      throw new IllegalArgumentException("There is no child mask for this animated mask");
    }
    this.masks.forEach(Mask::init);
    if (async) {
      this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, this::update, 0, this.period);
    } else {
      this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, this::update, 0, this.period);
    }
  }

  @Override
  public void stop() {
    if (this.task != null) {
      this.task.cancel();
    }
    this.masks.forEach(Mask::stop);
  }

  @Override
  public void update() {
    this.currentIndex = (this.currentIndex + 1) % this.masks.size();
  }

  /**
   * Get the list of masks
   *
   * @return the masks
   */
  public List<Mask> getMasks() {
    return masks;
  }
}
