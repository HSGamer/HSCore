package me.hsgamer.hscore.config.annotation.converter;

import java.util.Optional;

/**
 * A provider for the {@link Converter}
 */
public interface ConverterProvider {
  /**
   * Get the {@link Converter} for the type
   *
   * @param type the type
   *
   * @return the converter or empty if the type is not supported
   */
  Optional<Converter> getConverter(Class<?> type);
}
