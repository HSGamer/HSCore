package me.hsgamer.hscore.config.annotation.converter.manager;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.DefaultConverter;
import me.hsgamer.hscore.config.annotation.converter.PrimitiveConverter;
import me.hsgamer.hscore.config.annotation.converter.SimpleConverter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A manager to specify a default {@link Converter} for a type
 */
public final class DefaultConverterManager {
  private static final Map<Type, Converter> CONVERTER_MAP = new HashMap<>();

  static {
    registerConverter(String.class, new SimpleConverter(Objects::toString));
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
    registerConverter(URI.class, new Converter() {
      @Override
      public Object convert(Object raw) {
        if (raw == null) {
          return null;
        }
        return URI.create(Objects.toString(raw));
      }

      @Override
      public Object convertToRaw(Object value) {
        return value == null ? null : value.toString();
      }
    });
    registerConverter(URL.class, new Converter() {
      @Override
      public Object convert(Object raw) {
        if (raw == null) {
          return null;
        }
        try {
          return new URL(Objects.toString(raw));
        } catch (Exception e) {
          return null;
        }
      }

      @Override
      public Object convertToRaw(Object value) {
        return value == null ? null : value.toString();
      }
    });
    registerConverter(BigInteger.class, new Converter() {
      @Override
      public Object convert(Object raw) {
        if (raw == null) {
          return null;
        }
        try {
          return new BigInteger(Objects.toString(raw));
        } catch (Exception e) {
          return null;
        }
      }

      @Override
      public Object convertToRaw(Object value) {
        return value == null ? null : value.toString();
      }
    });
    registerConverter(BigDecimal.class, new Converter() {
      @Override
      public Object convert(Object raw) {
        if (raw == null) {
          return null;
        }
        try {
          return new BigDecimal(Objects.toString(raw));
        } catch (Exception e) {
          return null;
        }
      }

      @Override
      public Object convertToRaw(Object value) {
        return value == null ? null : value.toString();
      }
    });
    registerConverter(Instant.class, new Converter() {
      @Override
      public Object convert(Object raw) {
        if (raw == null) {
          return null;
        }
        try {
          return Instant.parse(Objects.toString(raw));
        } catch (Exception e) {
          return null;
        }
      }

      @Override
      public Object convertToRaw(Object value) {
        return value == null ? null : value.toString();
      }
    });
  }

  private DefaultConverterManager() {
    // EMPTY
  }

  /**
   * Register the converter for the type
   *
   * @param type      the type class
   * @param converter the converter
   */
  public static void registerConverter(Type type, Converter converter) {
    CONVERTER_MAP.put(type, converter);
  }

  /**
   * Unregister the converter for the type
   *
   * @param type the type
   */
  public static void unregisterConverter(Type type) {
    CONVERTER_MAP.remove(type);
  }

  /**
   * Get the converter for the type if the given converter is the default one
   *
   * @param type      the type
   * @param converter the converter
   *
   * @return the converter
   */
  public static Converter getConverterIfDefault(Type type, Converter converter) {
    if (converter instanceof DefaultConverter) {
      return CONVERTER_MAP.getOrDefault(type, converter);
    }
    return converter;
  }

  /**
   * Get the converter for the type if the given converter is the default one
   *
   * @param type           the type
   * @param converterClass the class of the converter
   *
   * @return the converter
   */
  public static Converter getConverterIfDefault(Type type, Class<? extends Converter> converterClass) {
    return getConverterIfDefault(type, Converter.createConverterSafe(converterClass));
  }

  /**
   * Get the converter for the type
   *
   * @param type the type
   *
   * @return the converter
   */
  public static Converter getConverter(Type type) {
    return getConverterIfDefault(type, new DefaultConverter());
  }
}
