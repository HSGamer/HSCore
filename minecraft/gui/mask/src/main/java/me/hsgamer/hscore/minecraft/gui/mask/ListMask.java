package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The mask with a list of child masks
 */
public class ListMask implements ButtonMap {
  private final List<ButtonMap> masks = new ArrayList<>();

  /**
   * Add mask(s)
   *
   * @param masks the mask
   * @param <T>   the type of the mask
   */
  public <T extends ButtonMap> void addMask(@NotNull Collection<@NotNull T> masks) {
    this.masks.addAll(masks);
  }

  /**
   * Add mask(s)
   *
   * @param mask the mask
   */
  public void addMask(@NotNull ButtonMap... mask) {
    addMask(Arrays.asList(mask));
  }

  /**
   * Get the list of masks
   *
   * @return the masks
   */
  @NotNull
  public List<@NotNull ButtonMap> getMasks() {
    return masks;
  }

  @Override
  public void init() {
    masks.forEach(ButtonMap::init);
  }

  @Override
  public void stop() {
    masks.forEach(ButtonMap::stop);
  }

  @Override
  public @Nullable Map<Integer, ActionItem> getItemMap(@NotNull InventoryContext context) {
    for (ButtonMap mask : masks) {
      Map<Integer, ActionItem> itemMap = mask.getItemMap(context);
      if (itemMap != null) {
        return itemMap;
      }
    }
    return null;
  }
}
