package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.MaskSlot;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A button paginated mask, where each {@link Button} is a page
 */
public abstract class SequencePaginatedMask extends PaginatedMask {
  protected final MaskSlot maskSlot;

  /**
   * Create a new mask
   *
   * @param name     the name of the mask
   * @param maskSlot the mask slot
   */
  protected SequencePaginatedMask(@NotNull String name, @NotNull MaskSlot maskSlot) {
    super(name);
    this.maskSlot = maskSlot;
  }

  /**
   * Get the mask slot
   *
   * @return the mask slot
   */
  @NotNull
  public MaskSlot getMaskSlot() {
    return this.maskSlot;
  }

  /**
   * Get the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the buttons
   */
  @NotNull
  public abstract List<@NotNull Button> getButtons(UUID uuid);

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid, int size) {
    List<Button> buttons = getButtons(uuid);
    List<Integer> slots = this.maskSlot.getSlots(uuid);
    if (buttons.isEmpty() || slots.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<Integer, Button> map = new HashMap<>();
    int basePage = this.getPage(uuid);
    int buttonsSize = buttons.size();
    int slotsSize = slots.size();

    for (int i = 0; i < slotsSize; i++) {
      int index = i + basePage;
      if (this.cycle) {
        index = this.getExactPage(index, uuid);
      } else if (index >= buttonsSize) {
        break;
      }
      map.put(slots.get(i), buttons.get(index));
    }
    return map;
  }

  @Override
  public int getPageAmount(@NotNull UUID uuid) {
    return getButtons(uuid).size();
  }

  @Override
  public void stop() {
    this.pageNumberMap.clear();
  }
}
