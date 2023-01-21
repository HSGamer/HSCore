package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface ButtonMap extends Initializable {
  @NotNull
  Map<@NotNull Button, @NotNull Collection<@NotNull Integer>> getButtons(@NotNull final UUID uuid);

  @NotNull
  default Button getDefaultButton(@NotNull UUID uuid) {
    return Button.EMPTY;
  }
}
