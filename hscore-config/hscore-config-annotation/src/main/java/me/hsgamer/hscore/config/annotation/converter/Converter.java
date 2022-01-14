package me.hsgamer.hscore.config.annotation.converter;

import me.hsgamer.hscore.config.annotation.ConfigPath;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * The converter for {@link ConfigPath} to convert the object to the raw value stored in the config and vice versa
 */
public interface Converter {

  /**
   * Create a new instance of the converter
   *
   * @param converterClass the class of the converter
   *
   * @return the new instance
   */
  @NotNull
  static Converter createConverterSafe(Class<? extends Converter> converterClass) {
    try {
      return converterClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

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
