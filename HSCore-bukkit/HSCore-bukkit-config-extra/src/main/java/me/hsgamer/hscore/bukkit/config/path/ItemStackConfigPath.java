package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.inventory.ItemStack;

public final class ItemStackConfigPath extends SerializableMapConfigPath<ItemStack> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public ItemStackConfigPath(final String path, final ItemStack def) {
    super(path, def);
  }

  @Override
  public ItemStack convert(final Map<String, Object> rawValue) {
    return ItemStack.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final ItemStack value) {
    return value.serialize();
  }

}
