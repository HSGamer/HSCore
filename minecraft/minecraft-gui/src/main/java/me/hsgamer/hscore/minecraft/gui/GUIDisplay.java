package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import me.hsgamer.hscore.ui.BaseDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The base {@link me.hsgamer.hscore.ui.Display} for UI in Minecraft
 *
 * @param <H> the type of the holder
 */
public abstract class GUIDisplay<H extends GUIHolder<?>> extends BaseDisplay<H> {

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
   * Open the display
   */
  public abstract void open();

  /**
   * Handle the event
   *
   * @param event the event
   */
  public abstract void handleEvent(ViewerEvent event);
}
