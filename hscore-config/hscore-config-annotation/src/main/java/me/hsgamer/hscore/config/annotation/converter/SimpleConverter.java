package me.hsgamer.hscore.config.annotation.converter;

import java.util.function.UnaryOperator;

/**
 * A simple {@link Converter} that use an operator to convert raw value to final value
 */
public class SimpleConverter implements Converter {
  private final UnaryOperator<Object> mapper;

  /**
   * Create a new converter
   *
   * @param mapper the operator
   */
  public SimpleConverter(UnaryOperator<Object> mapper) {
    this.mapper = mapper;
  }

  @Override
  public Object convert(Object raw) {
    if (raw == null) return null;
    return mapper.apply(raw);
  }

  @Override
  public Object convertToRaw(Object value) {
    return value;
  }
}
