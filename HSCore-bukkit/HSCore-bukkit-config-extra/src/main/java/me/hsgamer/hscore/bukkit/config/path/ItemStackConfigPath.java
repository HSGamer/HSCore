package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemStackConfigPath extends SerializableMapConfigPath<ItemStack> {

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
  public ItemStack convert(Map<String, Object> rawValue) {
    return ItemStack.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(ItemStack value) {
    return value.serialize();
  }
}
