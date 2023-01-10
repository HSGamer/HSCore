package me.hsgamer.hscore.config.annotation.converter;

import java.util.Objects;

/**
 * A {@link Converter} to convert primitive types
 */
public class PrimitiveConverter implements Converter {
  private final boolean isPrimitive;
  private final Class<?> boxedClass;

  public PrimitiveConverter(Class<?> clazz) {
    this.isPrimitive = clazz.isPrimitive();
    this.boxedClass = getBoxedClass(clazz);
    if (this.boxedClass == null) {
      throw new IllegalArgumentException("Not a primitive class: " + clazz);
    }
  }

  private static Class<?> getBoxedClass(Class<?> primitiveClass) {
    if (primitiveClass == boolean.class) {
      return Boolean.class;
    } else if (primitiveClass == byte.class) {
      return Byte.class;
    } else if (primitiveClass == short.class) {
      return Short.class;
    } else if (primitiveClass == int.class) {
      return Integer.class;
    } else if (primitiveClass == long.class) {
      return Long.class;
    } else if (primitiveClass == float.class) {
      return Float.class;
    } else if (primitiveClass == double.class) {
      return Double.class;
    } else if (primitiveClass == char.class) {
      return Character.class;
    }
    return null;
  }

  @Override
  public Object convert(Object raw) {
    if (raw == null) return null;
    if (boxedClass == raw.getClass()) return raw;
    String string = Objects.toString(raw, "");

    if (boxedClass == Boolean.class) {
      return isPrimitive ? Boolean.parseBoolean(string) : Boolean.valueOf(string);
    } else if (boxedClass == Byte.class) {
      try {
        return isPrimitive ? Byte.parseByte(string) : Byte.valueOf(string);
      } catch (Exception ignored) {
        return null;
      }
    } else if (boxedClass == Short.class) {
      try {
        return isPrimitive ? Short.parseShort(string) : Short.valueOf(string);
      } catch (Exception ignored) {
        return null;
      }
    } else if (boxedClass == Integer.class) {
      try {
        return isPrimitive ? Integer.parseInt(string) : Integer.valueOf(string);
      } catch (Exception ignored) {
        return null;
      }
    } else if (boxedClass == Long.class) {
      try {
        return isPrimitive ? Long.parseLong(string) : Long.valueOf(string);
      } catch (Exception ignored) {
        return null;
      }
    } else if (boxedClass == Float.class) {
      try {
        return isPrimitive ? Float.parseFloat(string) : Float.valueOf(string);
      } catch (Exception ignored) {
        return null;
      }
    } else if (boxedClass == Double.class) {
      try {
        return isPrimitive ? Double.parseDouble(string) : Double.valueOf(string);
      } catch (Exception ignored) {
        return null;
      }
    } else if (boxedClass == Character.class) {
      return isPrimitive ? string.charAt(0) : Character.valueOf(string.charAt(0));
    }
    return null;
  }

  @Override
  public Object convertToRaw(Object value) {
    return value;
  }
}
