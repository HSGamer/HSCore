package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AttributeModifierConfigPath extends SerializableMapConfigPath<AttributeModifier> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public AttributeModifierConfigPath(String path, AttributeModifier def) {
    super(path, def);
  }

  @Override
  public AttributeModifier convert(@NotNull final Map<String, Object> rawValue) {
    return AttributeModifier.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final AttributeModifier value) {
    return value.serialize();
  }
}
