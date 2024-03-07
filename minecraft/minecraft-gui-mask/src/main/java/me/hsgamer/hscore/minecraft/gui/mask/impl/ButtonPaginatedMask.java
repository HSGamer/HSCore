package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.MaskSlot;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The button paginated mask, those with a long list of {@link Button} divided into pages.
 */
public abstract class ButtonPaginatedMask extends PaginatedMask {
  private final MaskSlot maskSlot;

  /**
   * Create a new mask
   *
   * @param name     the name of the mask
   * @param maskSlot the mask slot
   */
  protected ButtonPaginatedMask(@NotNull String name, @NotNull MaskSlot maskSlot) {
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
    return maskSlot;
  }

  /**
   * Get the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the buttons
   */
  @NotNull
  public abstract List<@NotNull Button> getButtons(@NotNull UUID uuid);

  @Override
  protected Optional<Map<@NotNull Integer, @NotNull Button>> generateButtons(@NotNull UUID uuid, int size, int pageNumber) {
    List<Integer> slots = this.maskSlot.getSlots(uuid);
    List<Button> buttons = getButtons(uuid);
    if (buttons.isEmpty() || slots.isEmpty()) {
      return Optional.empty();
    }

    int pageAmount = (int) Math.ceil((double) getButtons(uuid).size() / slots.size());
    pageNumber = this.getAndSetExactPage(uuid, pageNumber, pageAmount);

    Map<Integer, Button> map = new HashMap<>();
    int slotsSize = slots.size();
    int offset = pageNumber * slotsSize;
    int buttonsSize = buttons.size();

    for (int i = 0; i < slotsSize; i++) {
      int index = i + offset;
      if (index >= buttonsSize) {
        break;
      }
      map.put(slots.get(i), buttons.get(index));
    }

    return Optional.of(map);
  }

  @Override
  public void stop() {
    this.pageNumberMap.clear();
  }
}
