package me.hsgamer.hscore.serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The serializer type.
 * It should be used on the serializer class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializerType {
  /**
   * The value of the serializer
   *
   * @return the value
   */
  String value();
}
