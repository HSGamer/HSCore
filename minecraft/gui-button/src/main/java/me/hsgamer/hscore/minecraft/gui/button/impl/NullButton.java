package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.DisplayButton;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * The null button, only with action
 */
public class NullButton implements Button {
  private final Consumer<@NotNull ClickEvent> consumer;

  /**
   * Create a new button
   *
   * @param consumer the consumer
   */
  public NullButton(@NotNull Consumer<@NotNull ClickEvent> consumer) {
    this.consumer = consumer;
  }

  @Override
  public @NotNull DisplayButton display(@NotNull UUID uuid) {
    return new DisplayButton().setClickAction(consumer);
  }
}
