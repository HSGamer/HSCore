package me.hsgamer.hscore.config.annotated.converter;

import me.hsgamer.hscore.config.annotated.AnnotatedConfig;

/**
 * The converter for {@link me.hsgamer.hscore.config.annotated.annotation.ConfigPath} to match the interaction between the field in {@link AnnotatedConfig} amd the config file it's assigned to
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
