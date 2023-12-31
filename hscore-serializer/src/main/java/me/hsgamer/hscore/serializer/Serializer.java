package me.hsgamer.hscore.serializer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * The serializer
 *
 * @param <I> the type of the input object
 * @param <O> the type of the output object
 * @param <D> the type of the data
 */
public abstract class Serializer<I, O, D> {
  private final Class<I> inputClass;
  private final Class<O> outputClass;
  private final Class<D> dataClass;

  private final List<Entry<I, D>> entryList = new ArrayList<>();

  /**
   * Create a new serializer
   *
   * @param inputClass  the class of the input
   * @param outputClass the class of the output
   * @param dataClass   the class of the data
   */
  protected Serializer(Class<I> inputClass, Class<O> outputClass, Class<D> dataClass) {
    this.inputClass = inputClass;
    this.outputClass = outputClass;
    this.dataClass = dataClass;
  }

  /**
   * Register a new data type
   *
   * @param type         the type
   * @param dataClass    the data class
   * @param dataFunction the data function
   */
  public void register(String type, Class<? extends D> dataClass, Function<I, D> dataFunction) {
    entryList.add(new Entry<>(type, dataClass, dataFunction));
  }

  /**
   * Register a new data type, without specifying the type.
   * The type will be the class name, or the value of {@link SerializerType} if the class is annotated with it.
   *
   * @param dataClass    the data class
   * @param dataFunction the data function
   */
  public void register(Class<? extends D> dataClass, Function<I, D> dataFunction) {
    String type;
    if (dataClass.isAnnotationPresent(SerializerType.class)) {
      type = dataClass.getAnnotation(SerializerType.class).value();
    } else {
      type = dataClass.getName();
    }
    register(type, dataClass, dataFunction);
  }

  /**
   * Register a new data type, without specifying the type and the data function.
   * The type will be the class name, or the value of {@link SerializerType} if the class is annotated with it.
   * The data function will be the static method annotated with {@link SerializerFunction}.
   *
   * @param dataClass the data class
   *
   * @throws IllegalArgumentException if the data function is not found
   */
  public void register(Class<? extends D> dataClass) {
    Function<I, D> dataFunction;
    try {
      Method method = null;
      for (Method m : dataClass.getDeclaredMethods()) {
        if (m.isAnnotationPresent(SerializerFunction.class)) {
          method = m;
          break;
        }
      }

      if (method == null) {
        throw new IllegalArgumentException("Cannot find data function in " + dataClass.getName());
      }

      if (!Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
        throw new IllegalArgumentException("The data function in " + dataClass.getName() + " must be static and public");
      }

      if (!dataClass.isAssignableFrom(method.getReturnType())) {
        throw new IllegalArgumentException("The data function in " + dataClass.getName() + " must return " + dataClass.getName());
      }

      if (method.getParameterCount() != 1 || !inputClass.isAssignableFrom(method.getParameterTypes()[0])) {
        throw new IllegalArgumentException("The data function in " + dataClass.getName() + " must have 1 parameter and the type must be " + inputClass.getName());
      }

      Method finalMethod = method;
      dataFunction = from -> {
        try {
          return dataClass.cast(finalMethod.invoke(null, from));
        } catch (Exception e) {
          throw new IllegalArgumentException(e);
        }
      };
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }

    register(dataClass, dataFunction);
  }

  /**
   * Unregister a data type
   *
   * @param type the type
   */
  public void unregister(String type) {
    entryList.removeIf(entry -> Objects.equals(entry.type, type));
  }

  /**
   * Unregister a data type
   *
   * @param dataClass the data class
   */
  public void unregister(Class<? extends D> dataClass) {
    entryList.removeIf(entry -> Objects.equals(entry.dataClass, dataClass));
  }

  /**
   * Convert the data to the output
   *
   * @param data  the data
   * @param input the input
   *
   * @return the output
   */
  protected abstract O convertOut(D data, I input);

  /**
   * Convert the data to the input
   *
   * @param data   the data
   * @param type   the type
   * @param output the output
   *
   * @return the input
   */
  protected abstract I convert(D data, String type, O output);

  /**
   * Get the data from the output
   *
   * @param output the output
   *
   * @return the data
   */
  protected abstract D getData(O output);

  /**
   * Get the type from the input
   *
   * @param input the input
   *
   * @return the type
   */
  protected abstract String getType(I input);

  /**
   * Deserialize the input
   *
   * @param input the input
   *
   * @return the output
   */
  public O deserialize(I input) {
    String type = getType(input);
    D data = entryList.stream()
      .filter(entry -> Objects.equals(entry.type, type))
      .findFirst()
      .map(entry -> entry.dataFunction.apply(input))
      .orElseThrow(() -> new IllegalArgumentException("Cannot find data type: " + type));
    return convertOut(data, input);
  }

  /**
   * Serialize the output
   *
   * @param to the output
   *
   * @return the input
   */
  public I serialize(O to) {
    D data = getData(to);
    Entry<I, D> entry = entryList.stream()
      .filter(e -> e.dataClass.isInstance(data))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Cannot find data class: " + data.getClass().getName()));
    return convert(data, entry.type, to);
  }

  private static class Entry<F, D> {
    private final String type;
    private final Class<? extends D> dataClass;
    private final Function<F, D> dataFunction;

    private Entry(String type, Class<? extends D> dataClass, Function<F, D> dataFunction) {
      this.type = type;
      this.dataClass = dataClass;
      this.dataFunction = dataFunction;
    }
  }
}
