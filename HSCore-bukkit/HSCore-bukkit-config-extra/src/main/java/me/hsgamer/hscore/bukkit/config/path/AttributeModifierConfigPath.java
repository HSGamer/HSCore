package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.attribute.AttributeModifier;

public final class AttributeModifierConfigPath extends SerializableMapConfigPath<AttributeModifier> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public AttributeModifierConfigPath(final String path, final AttributeModifier def) {
    super(path, def);
  }

  @Override
  public AttributeModifier convert(final Map<String, Object> rawValue) {
    return AttributeModifier.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final AttributeModifier value) {
    return value.serialize();
  }

}
