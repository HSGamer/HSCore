package me.hsgamer.hscore.bukkit.clicktype;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

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
  public AdvancedClickType(@NotNull final ClickType clickType, final int hotbarSlot) {
    this.clickType = clickType;
    this.hotbarSlot = hotbarSlot;
  }

  /**
   * New click type
   *
   * @param clickType the original click type
   */
  public AdvancedClickType(@NotNull final ClickType clickType) {
    this(clickType, -1);
  }

  /**
   * Get the original click type
   *
   * @return the Bukkit's click type
   */
  @NotNull
  public final ClickType getBukkitClickType() {
    return this.clickType;
  }

  /**
   * Get the hotbar slot if the ClickType
   *
   * @return the slot
   */
  public final int getHotbarSlot() {
    return this.hotbarSlot;
  }
}
