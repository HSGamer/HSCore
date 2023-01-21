package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Button extends Initializable {
  Button EMPTY = uuid -> null;

  @Nullable
  Item getItem(@NotNull final UUID uuid);

  default void handleAction(@NotNull final ClickEvent event) {
    // EMPTY
  }

  default boolean forceSetAction(@NotNull final UUID uuid) {
    return false;
  }
}
