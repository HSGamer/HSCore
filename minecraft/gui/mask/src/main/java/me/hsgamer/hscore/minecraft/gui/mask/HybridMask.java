package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The mask that views multiple masks
 */
public class HybridMask implements ButtonMap {
  private final List<ButtonMap> maskList = new ArrayList<>();

  /**
   * Add mask(s)
   *
   * @param masks the mask
   * @param <T>   the type of the mask
   */
  public <T extends ButtonMap> void addMask(@NotNull Collection<T> masks) {
    maskList.addAll(masks);
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
   * Get the masks
   *
   * @return the masks
   */
  public Collection<ButtonMap> getMasks() {
    return Collections.unmodifiableList(maskList);
  }

  @Override
  public void init() {
    maskList.forEach(ButtonMap::init);
  }

  @Override
  public void stop() {
    maskList.forEach(ButtonMap::stop);
  }

  @Override
  public @NotNull Map<Integer, ActionItem> getItemMap(@NotNull InventoryContext context) {
    Map<Integer, ActionItem> itemMap = new HashMap<>();
    for (ButtonMap mask : maskList) {
      Map<Integer, ActionItem> map = mask.getItemMap(context);
      if (map != null) {
        for (Map.Entry<Integer, ActionItem> entry : map.entrySet()) {
          ActionItem current = itemMap.getOrDefault(entry.getKey(), new ActionItem());
          current.apply(entry.getValue());
          itemMap.put(entry.getKey(), current);
        }
      }
    }
    return itemMap;
  }
}
