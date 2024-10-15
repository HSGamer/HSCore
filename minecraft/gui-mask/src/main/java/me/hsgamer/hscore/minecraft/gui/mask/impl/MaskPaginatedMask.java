package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The mask paginated mask, those with a long list of {@link Mask} divided into pages.
 */
public abstract class MaskPaginatedMask extends PaginatedMask {

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  protected MaskPaginatedMask(@NotNull String name) {
    super(name);
  }

  /**
   * Get the masks for the unique id
   *
   * @param uuid the unique id
   *
   * @return the masks
   */
  @NotNull
  public abstract List<@NotNull Mask> getMasks(@NotNull UUID uuid);

  @Override
  public Optional<Map<Integer, Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize, int pageNumber) {
    List<Mask> masks = getMasks(uuid);
    if (masks.isEmpty()) {
      return Optional.empty();
    }
    int pageAmount = masks.size();
    pageNumber = getAndSetExactPage(uuid, pageNumber, pageAmount);
    return masks.get(pageNumber).generateButtons(uuid, inventorySize);
  }

  @Override
  public void stop() {
    this.pageNumberMap.clear();
  }
}
