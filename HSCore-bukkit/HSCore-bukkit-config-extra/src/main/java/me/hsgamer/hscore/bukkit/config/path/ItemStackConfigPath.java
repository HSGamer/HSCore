package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class ItemStackConfigPath extends SerializableMapConfigPath<ItemStack> {
  private final Function<Map<String, Object>, ItemStack> deserializer;
  private final Function<ItemStack, Map<String, Object>> serializer;

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public ItemStackConfigPath(String path, ItemStack def) {
    this(path, def, ItemStack::deserialize, ItemStack::serialize);
  }

  public ItemStackConfigPath(String path, ItemStack def, Function<Map<String, Object>, ItemStack> deserializer, Function<ItemStack, Map<String, Object>> serializer) {
    super(path, def);
    this.deserializer = deserializer;
    this.serializer = serializer;
  }

  @Override
  public ItemStack convert(@NotNull final Map<String, Object> rawValue) {
    return this.deserializer.apply(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final ItemStack value) {
    return this.serializer.apply(value);
  }
}
