package me.hsgamer.hscore.ui;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A simple implementation of {@link Display}
 *
 * @param <H> the type of {@link Holder}
 */
public abstract class BaseDisplay<H extends Holder<?>> implements Display {
  protected final UUID uuid;
  protected final H holder;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  protected BaseDisplay(@NotNull UUID uuid, @NotNull H holder) {
    this.uuid = uuid;
    this.holder = holder;
  }

  @Override
  public @NotNull H getHolder() {
    return this.holder;
  }

  @Override
  public @NotNull UUID getUniqueId() {
    return this.uuid;
  }
}
