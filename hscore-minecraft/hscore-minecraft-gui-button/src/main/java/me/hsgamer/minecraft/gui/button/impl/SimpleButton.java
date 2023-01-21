package me.hsgamer.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleButton implements Button {
  private final Function<UUID, Item> itemFunction;
  private final Consumer<ClickEvent> consumer;

  public SimpleButton(@NotNull Function<@NotNull UUID, @Nullable Item> itemFunction, @NotNull Consumer<@NotNull ClickEvent> consumer) {
    this.itemFunction = itemFunction;
    this.consumer = consumer;
  }

  public SimpleButton(@Nullable Item item, @NotNull Consumer<@NotNull ClickEvent> consumer) {
    this(uuid -> item, consumer);
  }

  @Override
  public @Nullable Item getItem(@NotNull UUID uuid) {
    return itemFunction.apply(uuid);
  }

  @Override
  public void handleAction(@NotNull ClickEvent event) {
    consumer.accept(event);
  }
}
