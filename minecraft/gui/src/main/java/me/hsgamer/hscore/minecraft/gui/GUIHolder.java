package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import me.hsgamer.hscore.minecraft.gui.event.OpenEvent;
import me.hsgamer.hscore.ui.BaseHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The base {@link me.hsgamer.hscore.ui.Holder} for UI in Minecraft
 */
public abstract class GUIHolder<D extends GUIDisplay<?>> extends BaseHolder<D> {
  private @NotNull ButtonMap buttonMap = (uuid, size) -> Collections.emptyMap();

  /**
   * Get the button map
   *
   * @return the button map
   */
  @NotNull
  public ButtonMap getButtonMap() {
    return buttonMap;
  }

  /**
   * Set the button map
   *
   * @param buttonMap the button map
   */
  public void setButtonMap(@NotNull final ButtonMap buttonMap) {
    this.buttonMap = buttonMap;
  }

  @Override
  public void init() {
    addEventConsumer(OpenEvent.class, this::onOpen);

    addEventConsumer(ClickEvent.class, this::onClick);
    addEventConsumer(ClickEvent.class, event -> {
      if (event.isButtonExecute()) {
        getDisplay(event.getViewerID()).ifPresent(guiDisplay -> guiDisplay.handleEvent(event));
      }
    });

    addEventConsumer(CloseEvent.class, this::onClose);
    addEventConsumer(CloseEvent.class, event -> {
      if (event.isRemoveDisplay()) {
        removeDisplay(event.getViewerID());
      }
    });
    buttonMap.init();
  }

  @Override
  public void stop() {
    List<D> displays = new ArrayList<>(displayMap.values());
    super.stop();
    closeAll(displays);
    buttonMap.stop();
  }

  /**
   * Handle open event
   *
   * @param event the event
   */
  protected void onOpen(@NotNull final OpenEvent event) {
    // EMPTY
  }

  /**
   * Handle click event
   *
   * @param event the event
   */
  protected void onClick(@NotNull final ClickEvent event) {
    // EMPTY
  }

  /**
   * Handle close event
   *
   * @param event the event
   */
  protected void onClose(@NotNull final CloseEvent event) {
    // EMPTY
  }

  /**
   * Close all displays
   *
   * @param displays the list of closed displays to be closed
   */
  protected void closeAll(List<D> displays) {
    // EMPTY
  }
}
