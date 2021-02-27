package me.hsgamer.hscore.bukkit.gui.mask.multi;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.UUID;

/**
 * The interface for masks that works on multiple ids
 */
public interface MultiUserMask {
  /**
   * Set the button for the unique id
   *
   * @param uuid   the unique id
   * @param button the button
   */
  void setButton(UUID uuid, Button button);

  /**
   * Get the button for the unique id
   *
   * @param uuid the unique id
   *
   * @return the button
   */
  Button getButton(UUID uuid);
}
