package me.hsgamer.hscore.bukkit.clicktype;

import org.bukkit.event.inventory.ClickType;

/**
 * {@link ClickType} with hotbar support
 */
public class AdvancedClickType {

  private final ClickType clickType;
  private final int hotbarSlot;

  /**
   * New click type
   *
   * @param clickType  the original click type
   * @param hotbarSlot the slot
   */
  public AdvancedClickType(ClickType clickType, int hotbarSlot) {
    this.clickType = clickType;
    this.hotbarSlot = hotbarSlot;
  }

  /**
   * New click type
   *
   * @param clickType the original click type
   */
  public AdvancedClickType(ClickType clickType) {
    this(clickType, -1);
  }

  /**
   * Get the original click type
   *
   * @return the Bukkit's click type
   */
  public ClickType getBukkitClickType() {
    return clickType;
  }

  /**
   * Get the hotbar slot if the ClickType
   *
   * @return the slot
   */
  public int getHotbarSlot() {
    return hotbarSlot;
  }
}
