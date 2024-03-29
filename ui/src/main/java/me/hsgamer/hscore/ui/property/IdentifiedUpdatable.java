package me.hsgamer.hscore.ui.property;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The interface for some classes than can be updated with the identifier
 */
public interface IdentifiedUpdatable {
  /**
   * Update the updatable object
   *
   * @param uuid the identifier
   */
  default void update(@NotNull UUID uuid) {
    // EMPTY
  }
}
