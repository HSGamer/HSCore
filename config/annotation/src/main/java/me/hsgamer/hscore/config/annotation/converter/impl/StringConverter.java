package me.hsgamer.hscore.config.annotation.converter.impl;

import me.hsgamer.hscore.config.annotation.converter.Converter;

import java.util.Objects;
import java.util.function.Function;

/**
 * A simple {@link Converter} for {@link String} to any object
 *
 * @param <T> the type of the object
 */
public abstract class StringConverter<T> implements Converter {
  /**
   * Create a new instance of the converter
   *
   * @param from the function to convert the string to the object
   * @param to   the function to convert the object to the string
   * @param <T>  the type of the object
   *
   * @return the new instance of the converter
   */
  public static <T> StringConverter<T> of(Function<String, T> from, Function<T, String> to) {
    return new StringConverter<T>() {
      @Override
      protected T convertFrom(String string) {
        return from.apply(string);
      }

      @Override
      protected String convertTo(T t) {
        return to.apply(t);
      }
    };
  }

  /**
   * Convert the string to the object
   *
   * @param string the string
   *
   * @return the object
   */
  protected abstract T convertFrom(String string);

  /**
   * Convert the object to the string
   *
   * @param t the object
   *
   * @return the string
   */
  protected abstract String convertTo(T t);

  @Override
  public Object convert(Object raw) {
    if (raw == null) {
      return null;
    }
    try {
      return convertFrom(Objects.toString(raw));
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public Object convertToRaw(Object value) {
    if (value == null) {
      return null;
    }
    try {
      //noinspection unchecked
      return convertTo((T) value);
    } catch (Exception e) {
      return null;
    }
  }
}
