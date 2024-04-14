package me.hsgamer.hscore.config.annotation.converter.manager;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.ConverterProvider;
import me.hsgamer.hscore.config.annotation.converter.impl.*;

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
    registerConverter(URI.class, StringConverter.of(URI::create, URI::toString));
    registerConverter(URL.class, StringConverter.of(s -> {
      try {
        return new URL(s);
      } catch (Exception e) {
        return null;
      }
    }, URL::toString));
    registerConverter(BigInteger.class, StringConverter.of(BigInteger::new, BigInteger::toString));
    registerConverter(BigDecimal.class, StringConverter.of(BigDecimal::new, BigDecimal::toString));
    registerConverter(Instant.class, StringConverter.of(Instant::parse, Instant::toString));
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
      if (CONVERTER_MAP.containsKey(type)) {
        return CONVERTER_MAP.get(type);
      }

      if (type instanceof Class) {
        Class<?> clazz = (Class<?>) type;
        for (ConverterProvider provider : PROVIDERS) {
          Optional<Converter> optionalConverter = provider.getConverter(clazz);
          if (optionalConverter.isPresent()) {
            Converter finalConverter = optionalConverter.get();
            CONVERTER_MAP.put(type, finalConverter);
            return finalConverter;
          }
        }
      }
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
