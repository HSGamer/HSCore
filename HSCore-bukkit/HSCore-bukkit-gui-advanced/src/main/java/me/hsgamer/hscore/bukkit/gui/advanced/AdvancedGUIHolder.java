package me.hsgamer.hscore.bukkit.gui.advanced;

import me.hsgamer.hscore.bukkit.gui.GUIHolder;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The advanced UI Holder for Bukkit (Only accepts {@link InventoryType#CHEST}
 */
public class AdvancedGUIHolder extends GUIHolder<AdvancedGUIDisplay> {
  private final List<Mask> masks = new LinkedList<>();

  protected AdvancedGUIHolder(Plugin plugin, boolean removeDisplayOnClose) {
    super(plugin, removeDisplayOnClose);
  }

  protected AdvancedGUIHolder(Plugin plugin) {
    super(plugin);
  }

  /**
   * Add a mask
   *
   * @param mask the mask
   */
  public void addMask(Mask mask) {
    masks.add(mask);
  }

  /**
   * Remove masks by name
   *
   * @param name the name of the mask
   */
  public void removeMask(String name) {
    masks.removeIf(mask -> mask.getName().equals(name));
  }

  /**
   * Remove all masks
   *
   * @return the removed masks
   */
  public Collection<Mask> removeAllMasks() {
    List<Mask> removedMasks = new LinkedList<>(this.masks);
    masks.clear();
    return removedMasks;
  }

  /**
   * Get masks by name
   *
   * @param name the name of the mask
   *
   * @return the list of masks
   */
  public List<Mask> getMasks(String name) {
    return masks.parallelStream().filter(mask -> mask.getName().equals(name)).collect(Collectors.toList());
  }

  /**
   * Get all masks
   *
   * @return the list of all masks
   */
  public List<Mask> getMasks() {
    return Collections.unmodifiableList(masks);
  }

  @Override
  public void setInventoryType(InventoryType inventoryType) {
    throw new UnsupportedOperationException("This holder applies CHEST only");
  }

  @Override
  protected AdvancedGUIDisplay newDisplay(UUID uuid) {
    return new AdvancedGUIDisplay(uuid, this);
  }

  @Override
  public void stop() {
    removeAllMasks().forEach(Mask::stop);
    super.stop();
  }
}
