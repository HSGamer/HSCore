package me.hsgamer.hscore.config.annotation.converter.impl;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.ConverterProvider;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Optional;

/**
 * A {@link Converter} to convert an array
 */
public class DefaultArrayConverterProvider implements ConverterProvider {
  @Override
  public Optional<Converter> getConverter(Class<?> type) {
    Class<?> componentClass = type.getComponentType();
    if (componentClass == null) return Optional.empty();
    Converter converter = DefaultConverterManager.getConverter(componentClass);

    Converter arrayConverter = new Converter() {
      @Override
      public Object convert(Object raw) {
        if (raw == null) {
          return null;
        }
        Class<?> rawClass = raw.getClass();
        if (rawClass.isArray()) {
          Object parsedArray = Array.newInstance(componentClass, Array.getLength(raw));
          for (int i = 0; i < Array.getLength(raw); i++) {
            Object value = Array.get(raw, i);
            Object parsedValue = converter.convert(value);
            Array.set(parsedArray, i, parsedValue);
          }
          return parsedArray;
        } else if (raw instanceof Collection) {
          Collection<?> collection = (Collection<?>) raw;
          Object parsedArray = Array.newInstance(componentClass, collection.size());
          int size = collection.size();
          for (int i = 0; i < size; i++) {
            Object value = collection.toArray()[i];
            Object parsedValue = converter.convert(value);
            Array.set(parsedArray, i, parsedValue);
          }
          return parsedArray;
        } else {
          return null;
        }
      }

      @Override
      public Object convertToRaw(Object value) {
        if (value == null) {
          return null;
        }
        Class<?> valueClass = value.getClass();
        if (valueClass.isArray()) {
          Object[] rawArray = new Object[Array.getLength(value)];
          for (int i = 0; i < Array.getLength(value); i++) {
            Object element = Array.get(value, i);
            Object rawElement = converter.convertToRaw(element);
            rawArray[i] = rawElement;
          }
          return rawArray;
        } else {
          return null;
        }
      }
    };

    return Optional.of(arrayConverter);
  }
}
