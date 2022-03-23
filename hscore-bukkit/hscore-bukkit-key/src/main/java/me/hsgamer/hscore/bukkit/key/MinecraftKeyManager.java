package me.hsgamer.hscore.bukkit.key;

import org.bukkit.NamespacedKey;

/**
 * the {@link KeyManager} for default Minecraft
 */
public class MinecraftKeyManager extends AbstractKeyManager {
  @Override
  public NamespacedKey newKey(String key) {
    return NamespacedKey.minecraft(key);
  }
}
