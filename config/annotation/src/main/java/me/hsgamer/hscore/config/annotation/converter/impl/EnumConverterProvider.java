package me.hsgamer.hscore.config.annotation.converter.impl;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.ConverterProvider;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Converter} to convert enum types
 */
public class EnumConverterProvider implements ConverterProvider {
  @Override
  public Optional<Converter> getConverter(Class<?> type) {
    if (!type.isEnum()) return Optional.empty();

    return Optional.of(new Converter() {
      @Override
      public Object convert(Object raw) {
        if (raw == null) {
          return null;
        }
        String rawValue = Objects.toString(raw).toUpperCase(Locale.ROOT);
        try {
          //noinspection unchecked,rawtypes
          return Enum.valueOf((Class<Enum>) type, rawValue);
        } catch (Exception e) {
          return null;
        }
      }

      @Override
      public Object convertToRaw(Object value) {
        if (value == null) {
          return null;
        }
        if (value instanceof Enum) {
          return ((Enum<?>) value).name();
        }
        return null;
      }
    });
  }
}
