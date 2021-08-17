package me.hsgamer.hscore.config.converter;

/**
 * The default {@link Converter}, which does nothing, so the raw value and the final value are the same type
 */
public class DefaultConverter implements Converter {

  @Override
  public Object convert(Object raw) {
    return raw;
  }

  @Override
  public Object convertToRaw(Object value) {
    return value;
  }
}
