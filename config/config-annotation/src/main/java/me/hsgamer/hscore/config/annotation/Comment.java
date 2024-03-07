package me.hsgamer.hscore.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Comment annotation for {@link ConfigPath}
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Comment {
  /**
   * Get the comment
   *
   * @return the comment
   */
  String[] value();
}
