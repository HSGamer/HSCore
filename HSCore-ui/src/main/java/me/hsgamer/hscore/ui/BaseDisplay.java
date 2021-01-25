package me.hsgamer.hscore.ui;

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
  protected BaseDisplay(UUID uuid, H holder) {
    this.uuid = uuid;
    this.holder = holder;
  }

  @Override
  public H getHolder() {
    return this.holder;
  }

  @Override
  public UUID getUniqueId() {
    return this.uuid;
  }
}
