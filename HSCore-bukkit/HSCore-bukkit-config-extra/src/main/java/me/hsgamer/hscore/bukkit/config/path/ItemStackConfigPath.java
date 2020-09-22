package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ItemStackConfigPath extends SerializableMapConfigPath<ItemStack> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public ItemStackConfigPath(String path, ItemStack def) {
    super(path, def);
  }

  @Override
  public ItemStack convert(@NotNull final Map<String, Object> rawValue) {
    return ItemStack.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final ItemStack value) {
    return value.serialize();
  }
}
