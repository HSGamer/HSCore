package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import me.hsgamer.hscore.minecraft.gui.mask.util.MaskSlot;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The masks with multiple slot
 */
public class MultiSlotsMask implements ButtonMap {
  protected final MaskSlot maskSlot;
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param maskSlot the mask slot
   */
  public MultiSlotsMask(@NotNull MaskSlot maskSlot) {
    this.maskSlot = maskSlot;
  }

  /**
   * Add button(s)
   *
   * @param buttons the buttons
   * @param <T>     the type of the button
   */
  public <T extends Button> void addButton(@NotNull Collection<@NotNull T> buttons) {
    this.buttons.addAll(buttons);
  }

  /**
   * Add button(s)
   *
   * @param button the button
   */
  public void addButton(@NotNull Button... button) {
    addButton(Arrays.asList(button));
  }

  /**
   * Get the mask slot
   *
   * @return the mask slot
   */
  @NotNull
  public MaskSlot getMaskSlot() {
    return maskSlot;
  }

  /**
   * Get the buttons
   *
   * @return the buttons
   */
  @NotNull
  public List<@NotNull Button> getButtons() {
    return Collections.unmodifiableList(buttons);
  }

  @Override
  public void init() {
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.buttons.forEach(Button::stop);
  }

  @Override
  public @NotNull Map<Integer, ActionItem> getItemMap(InventoryContext context) {
    Map<Integer, ActionItem> map = new HashMap<>();
    List<Integer> slots = this.maskSlot.apply(context);
    if (!this.buttons.isEmpty() && !slots.isEmpty()) {
      int slotsSize = slots.size();
      int buttonsSize = this.buttons.size();
      for (int i = 0; i < slotsSize; i++) {
        ActionItem item = this.buttons.get(i % buttonsSize).getItem(context);
        if (item != null) {
          map.put(slots.get(i), item);
        }
      }
    }
    return map;
  }
}
