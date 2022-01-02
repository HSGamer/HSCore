package me.hsgamer.hscore.config.annotation;

import me.hsgamer.hscore.config.converter.Converter;
import me.hsgamer.hscore.config.converter.DefaultConverter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The main annotation to set the config path for the fields in {@link me.hsgamer.hscore.config.AnnotatedConfig}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigPath {
  /**
   * Get the config path
   *
   * @return the path
   */
  @NotNull String value();

  /**
   * Get the converter
   *
   * @return the converter
   */
  @NotNull Class<? extends Converter> converter() default DefaultConverter.class;
}
