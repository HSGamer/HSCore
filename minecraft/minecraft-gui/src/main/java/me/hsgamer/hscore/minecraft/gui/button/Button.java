package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * An interface for all buttons
 */
public interface Button extends Initializable {
  /**
   * The empty button
   */
  Button EMPTY = uuid -> null;

  /**
   * Get the item for the unique id
   *
   * @param uuid the unique id
   *
   * @return the item
   */
  @Nullable
  Item getItem(@NotNull final UUID uuid);

  /**
   * Handle the click event
   *
   * @param event the click event
   */
  default void handleAction(@NotNull final ClickEvent event) {
    // EMPTY
  }

  /**
   * Check if the action of this button should be set even if the display item is null
   *
   * @param uuid the unique id
   *
   * @return true if it should
   */
  default boolean forceSetAction(@NotNull final UUID uuid) {
    return false;
  }
}
