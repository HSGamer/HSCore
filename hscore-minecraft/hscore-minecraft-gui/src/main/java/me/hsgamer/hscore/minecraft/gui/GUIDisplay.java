package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import me.hsgamer.hscore.ui.BaseDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The base {@link me.hsgamer.hscore.ui.Display} for UI in Minecraft
 *
 * @param <H> the type of the holder
 */
public abstract class GUIDisplay<H extends GUIHolder<?>> extends BaseDisplay<H> {
  /**
   * The viewed buttons
   */
  protected final Map<@NotNull Integer, @NotNull Button> viewedButtons = new ConcurrentHashMap<>();

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  protected GUIDisplay(@NotNull final UUID uuid, @NotNull final H holder) {
    super(uuid, holder);
  }

  /**
   * Schedule a task to re-open the display
   *
   * @param event the close event
   */
  public abstract void scheduleReopen(CloseEvent event);

  /**
   * Open the display
   */
  public abstract void open();

  /**
   * Handle the click event
   *
   * @param event the click event
   */
  public void handleClickEvent(@NotNull final ClickEvent event) {
    Optional.ofNullable(viewedButtons.get(event.getSlot())).ifPresent(button -> button.handleAction(event));
  }

  /**
   * Get the viewed button at the slot
   *
   * @param slot the slot
   *
   * @return the viewed button
   */
  public Optional<Button> getViewedButton(int slot) {
    return Optional.ofNullable(viewedButtons.get(slot));
  }

  /**
   * Get the viewed buttons
   *
   * @return the viewed buttons
   */
  public Map<Integer, Button> getViewedButtons() {
    return Collections.unmodifiableMap(viewedButtons);
  }
}
