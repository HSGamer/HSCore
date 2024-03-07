package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * The null button, only with action
 */
public class NullButton extends SimpleButton {
  /**
   * Create a new button
   *
   * @param consumer the consumer
   */
  public NullButton(@NotNull Consumer<@NotNull ClickEvent> consumer) {
    super(uuid -> null, consumer);
  }

  @Override
  public boolean forceSetAction(@NotNull UUID uuid) {
    return true;
  }
}
