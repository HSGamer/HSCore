package me.hsgamer.hscore.config.annotation.converter;

import me.hsgamer.hscore.config.annotation.ConfigPath;

/**
 * The converter for {@link ConfigPath}
 */
public interface Converter {
  /**
   * Convert the raw value from the config file to the final value for the field
   *
   * @param raw the raw value
   *
   * @return the final value
   */
  Object convert(Object raw);

  /**
   * Convert the final value from the field to the raw value for the config file
   *
   * @param value the final value
   *
   * @return the raw value
   */
  Object convertToRaw(Object value);
}
