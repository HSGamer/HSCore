package me.hsgamer.hscore.config.annotation.converter.manager;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.ConverterProvider;
import me.hsgamer.hscore.config.annotation.converter.impl.DefaultArrayConverterProvider;
import me.hsgamer.hscore.config.annotation.converter.impl.DefaultConverter;
import me.hsgamer.hscore.config.annotation.converter.impl.PrimitiveConverterProvider;
import me.hsgamer.hscore.config.annotation.converter.impl.SimpleConverter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.*;

/**
 * A manager to specify a default {@link Converter} for a type
 */
public final class DefaultConverterManager {
  private static final List<ConverterProvider> PROVIDERS = new ArrayList<>();
  private static final Map<Type, Converter> CONVERTER_MAP = new HashMap<>();

  static {
    registerProvider(new PrimitiveConverterProvider());
    registerProvider(new DefaultArrayConverterProvider());
    registerConverter(String.class, new SimpleConverter(Objects::toString));
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
   * Register a converter provider
   *
   * @param provider the provider
   */
  public static void registerProvider(ConverterProvider provider) {
    PROVIDERS.add(provider);
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
      return CONVERTER_MAP.computeIfAbsent(type, key -> {
        if (key instanceof Class) {
          Class<?> clazz = (Class<?>) key;
          for (ConverterProvider provider : PROVIDERS) {
            Optional<Converter> optionalConverter = provider.getConverter(clazz);
            if (optionalConverter.isPresent()) {
              return optionalConverter.get();
            }
          }
        }
        return converter;
      });
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
