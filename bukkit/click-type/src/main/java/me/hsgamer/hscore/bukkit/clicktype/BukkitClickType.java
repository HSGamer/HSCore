package me.hsgamer.hscore.bukkit.clicktype;

import me.hsgamer.hscore.minecraft.clicktype.MinecraftClickType;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

/**
 * {@link MinecraftClickType} for Bukkit
 */
public class BukkitClickType implements MinecraftClickType {
  private final ClickType clickType;
  private final int hotbarSlot;

  /**
   * New click type
   *
   * @param clickType  the original click type
   * @param hotbarSlot the slot
   */
  public BukkitClickType(@NotNull final ClickType clickType, final int hotbarSlot) {
    this.clickType = clickType;
    this.hotbarSlot = hotbarSlot;
  }

  /**
   * New click type
   *
   * @param clickType the original click type
   */
  public BukkitClickType(@NotNull final ClickType clickType) {
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

  @Override
  public String getName() {
    if (clickType.equals(ClickType.NUMBER_KEY)) {
      return clickType.name() + "_" + hotbarSlot;
    }
    return clickType.name();
  }

  @Override
  public boolean isLeftClick() {
    return clickType.isLeftClick();
  }

  @Override
  public boolean isRightClick() {
    return clickType.isRightClick();
  }

  @Override
  public boolean isShiftClick() {
    return clickType.isShiftClick();
  }

  @Override
  public boolean isNumberClick() {
    return clickType == ClickType.NUMBER_KEY;
  }

  @Override
  public int getHotbarSlot() {
    return hotbarSlot;
  }
}
