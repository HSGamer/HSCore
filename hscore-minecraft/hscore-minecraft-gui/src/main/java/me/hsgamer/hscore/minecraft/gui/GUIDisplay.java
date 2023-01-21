package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import me.hsgamer.hscore.ui.BaseDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GUIDisplay<H extends GUIHolder<?>> extends BaseDisplay<H> {
  protected final Map<@NotNull Integer, @NotNull Button> viewedButtons = new ConcurrentHashMap<>();

  protected GUIDisplay(@NotNull final UUID uuid, @NotNull final H holder) {
    super(uuid, holder);
  }

  public abstract void scheduleReopen(CloseEvent event);

  public abstract void open();

  public void handleClickEvent(@NotNull final ClickEvent event) {
    Optional.ofNullable(viewedButtons.get(event.getSlot())).ifPresent(button -> button.handleAction(event));
  }
}
