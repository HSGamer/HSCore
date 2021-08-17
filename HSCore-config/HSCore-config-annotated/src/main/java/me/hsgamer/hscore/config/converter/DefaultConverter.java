package me.hsgamer.hscore.config.converter;

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
