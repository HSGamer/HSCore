package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The button paginated mask, those with a long list of {@link Button} divided into pages.
 */
public abstract class ButtonPaginatedMask extends PaginatedMask {
  protected final List<Integer> slots = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param slots the slots
   */
  protected ButtonPaginatedMask(@NotNull String name, @NotNull List<@NotNull Integer> slots) {
    super(name);
    this.slots.addAll(slots);
  }

  /**
   * Get the slots
   *
   * @return the slots
   */
  @NotNull
  public List<@NotNull Integer> getSlots() {
    return Collections.unmodifiableList(slots);
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
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
    List<Button> buttons = getButtons(uuid);
    if (buttons.isEmpty() || this.slots.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<Integer, Button> map = new HashMap<>();
    int pageNumber = this.getPage(uuid);
    int slotsSize = this.slots.size();
    int offset = pageNumber * slotsSize;
    int buttonsSize = buttons.size();

    for (int i = 0; i < slotsSize; i++) {
      int index = i + offset;
      if (index >= buttonsSize) {
        break;
      }
      map.put(this.slots.get(i), buttons.get(index));
    }
    return map;
  }

  @Override
  public void stop() {
    this.slots.clear();
    this.pageNumberMap.clear();
  }

  @Override
  public int getPageAmount(@NotNull UUID uuid) {
    return this.slots.isEmpty() ? 0 : (int) Math.ceil((double) getButtons(uuid).size() / this.slots.size());
  }
}
