package me.hsgamer.hscore.config.annotation.converter.manager;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.DefaultConverter;
import me.hsgamer.hscore.config.annotation.converter.PrimitiveConverter;
import me.hsgamer.hscore.config.annotation.converter.SimpleConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A manager to specify a default {@link Converter} for a type class
 */
public final class DefaultConverterManager {
  private static final Map<Class<?>, Converter> CONVERTER_MAP = new HashMap<>();

  static {
    registerConverter(String.class, new SimpleConverter(o -> Objects.toString(o, null)));
    registerConverter(boolean.class, new PrimitiveConverter(boolean.class));
    registerConverter(Boolean.class, new PrimitiveConverter(Boolean.class));
    registerConverter(byte.class, new PrimitiveConverter(byte.class));
    registerConverter(Byte.class, new PrimitiveConverter(Byte.class));
    registerConverter(short.class, new PrimitiveConverter(short.class));
    registerConverter(Short.class, new PrimitiveConverter(Short.class));
    registerConverter(int.class, new PrimitiveConverter(int.class));
    registerConverter(Integer.class, new PrimitiveConverter(Integer.class));
    registerConverter(long.class, new PrimitiveConverter(long.class));
    registerConverter(Long.class, new PrimitiveConverter(Long.class));
    registerConverter(float.class, new PrimitiveConverter(float.class));
    registerConverter(Float.class, new PrimitiveConverter(Float.class));
    registerConverter(double.class, new PrimitiveConverter(double.class));
    registerConverter(Double.class, new PrimitiveConverter(Double.class));
    registerConverter(char.class, new PrimitiveConverter(char.class));
    registerConverter(Character.class, new PrimitiveConverter(Character.class));
  }

  private DefaultConverterManager() {
    // EMPTY
  }

  /**
   * Register the converter for the type class
   *
   * @param clazz     the type class
   * @param converter the converter
   */
  public static void registerConverter(Class<?> clazz, Converter converter) {
    CONVERTER_MAP.put(clazz, converter);
  }

  /**
   * Unregister the converter for the type class
   *
   * @param clazz the type class
   */
  public static void unregisterConverter(Class<?> clazz) {
    CONVERTER_MAP.remove(clazz);
  }

  /**
   * Get the converter for the type class if the given converter is the default one
   *
   * @param clazz     the type class
   * @param converter the converter
   *
   * @return the converter
   */
  public static Converter getConverterIfDefault(Class<?> clazz, Converter converter) {
    if (converter instanceof DefaultConverter) {
      return CONVERTER_MAP.getOrDefault(clazz, converter);
    }
    return converter;
  }

  /**
   * Get the converter for the type class if the given converter is the default one
   *
   * @param clazz          the type class
   * @param converterClass the class of the converter
   *
   * @return the converter
   */
  public static Converter getConverterIfDefault(Class<?> clazz, Class<? extends Converter> converterClass) {
    return getConverterIfDefault(clazz, Converter.createConverterSafe(converterClass));
  }

  /**
   * Get the converter for the type class
   *
   * @param clazz the type class
   *
   * @return the converter
   */
  public static Converter getConverter(Class<?> clazz) {
    return getConverterIfDefault(clazz, new DefaultConverter());
  }
}
