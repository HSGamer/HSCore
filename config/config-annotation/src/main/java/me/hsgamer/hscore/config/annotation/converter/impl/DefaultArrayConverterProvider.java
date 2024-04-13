package me.hsgamer.hscore.config.annotation.converter.impl;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.ConverterProvider;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * A {@link Converter} to convert an array
 */
public class DefaultArrayConverterProvider implements ConverterProvider {
  @Override
  public Optional<Converter> getConverter(Class<?> type) {
    return Optional.ofNullable(type.getComponentType())
      .map(DefaultConverterManager::getConverter)
      .map(converter -> new Converter() {
        @Override
        public Object convert(Object raw) {
          if (raw == null) {
            return null;
          }
          if (raw instanceof Object[]) {
            return Arrays.stream((Object[]) raw)
              .map(converter::convert)
              .toArray(Object[]::new);
          } else if (raw instanceof Collection) {
            return ((Collection<?>) raw).stream()
              .map(converter::convert)
              .toArray(Object[]::new);
          } else {
            return null;
          }
        }

        @Override
        public Object convertToRaw(Object value) {
          if (value == null) {
            return null;
          }
          if (value instanceof Object[]) {
            return Arrays.stream((Object[]) value)
              .map(converter::convertToRaw)
              .toArray();
          } else {
            return null;
          }
        }
      });
  }
}
