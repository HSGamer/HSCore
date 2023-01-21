package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import me.hsgamer.hscore.minecraft.gui.event.OpenEvent;
import me.hsgamer.hscore.ui.BaseHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class GUIHolder<D extends GUIDisplay<?>> extends BaseHolder<D> {
  private boolean removeDisplayOnClose = true;
  private @NotNull Predicate<UUID> closeFilter = uuid -> true;
  private @NotNull ButtonMap buttonMap = uuid -> Collections.emptyMap();

  public boolean isRemoveDisplayOnClose() {
    return removeDisplayOnClose;
  }

  public void setRemoveDisplayOnClose(boolean removeDisplayOnClose) {
    this.removeDisplayOnClose = removeDisplayOnClose;
  }

  @NotNull
  public Predicate<UUID> getCloseFilter() {
    return closeFilter;
  }

  public void setCloseFilter(@NotNull final Predicate<UUID> closeFilter) {
    this.closeFilter = closeFilter;
  }

  @NotNull
  public ButtonMap getButtonMap() {
    return buttonMap;
  }

  public void setButtonMap(@NotNull final ButtonMap buttonMap) {
    this.buttonMap = buttonMap;
  }

  @Override
  public void init() {
    addEventConsumer(CloseEvent.class, event -> {
      UUID uuid = event.getViewerID();

      Optional<D> optionalDisplay = getDisplay(uuid);
      if (!optionalDisplay.isPresent()) {
        return;
      }
      D display = optionalDisplay.get();

      if (!closeFilter.test(uuid)) {
        display.scheduleReopen();
      } else if (removeDisplayOnClose) {
        removeDisplay(uuid);
      }
    });

    addEventConsumer(OpenEvent.class, this::onOpen);
    addEventConsumer(ClickEvent.class, this::onClick);
    addEventConsumer(CloseEvent.class, this::onClose);

    addEventConsumer(ClickEvent.class, event -> getDisplay(event.getViewerID()).ifPresent(guiDisplay -> guiDisplay.handleClickEvent(event)));
    buttonMap.init();
  }

  @Override
  public void stop() {
    super.stop();
    buttonMap.stop();
  }

  protected void onOpen(@NotNull final OpenEvent event) {
    // EMPTY
  }

  protected void onClick(@NotNull final ClickEvent event) {
    // EMPTY
  }

  protected void onClose(@NotNull final CloseEvent event) {
    // EMPTY
  }
}
