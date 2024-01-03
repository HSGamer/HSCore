package me.hsgamer.hscore.serializer;

import me.hsgamer.hscore.serializer.annotation.SerializerInputFunction;
import me.hsgamer.hscore.serializer.annotation.SerializerOutputFunction;
import me.hsgamer.hscore.serializer.annotation.SerializerType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The serializer
 *
 * @param <I> the type of the input object
 * @param <O> the type of the output object
 */
public class Serializer<I, O> {
  private final List<Entry<I, O>> entryList = new ArrayList<>();

  /**
   * Register a new entry
   *
   * @param type           the type
   * @param outputClass    the class of the output
   * @param outputFunction the output function
   * @param inputFunction  the input function
   * @param <T>            the type of the output
   *
   * @return the serializer
   */
  public <T extends O> Serializer<I, O> register(String type, Class<T> outputClass, Function<I, T> outputFunction, Function<T, I> inputFunction) {
    entryList.add(new Entry<>(type, outputClass, outputFunction::apply, input -> inputFunction.apply(outputClass.cast(input))));
    return this;
  }

  /**
   * Register a new entry, without specifying the type.
   * The type will be the class name of the output, or the value of {@link SerializerType} if the class is annotated with it.
   *
   * @param outputClass    the class of the output
   * @param outputFunction the output function
   * @param inputFunction  the input function
   * @param <T>            the type of the output
   *
   * @return the serializer
   */
  public <T extends O> Serializer<I, O> register(Class<T> outputClass, Function<I, T> outputFunction, Function<T, I> inputFunction) {
    String type;
    if (outputClass.isAnnotationPresent(SerializerType.class)) {
      type = outputClass.getAnnotation(SerializerType.class).value();
    } else {
      type = outputClass.getName();
    }
    return register(type, outputClass, outputFunction, inputFunction);
  }

  /**
   * Register a new entry, without specifying the type and the input function.
   * The type will be the class name of the output, or the value of {@link SerializerType} if the class is annotated with it.
   * The input function will be the method annotated with {@link SerializerInputFunction} in the output class, which must be public and non-static.
   * The output function will be the method annotated with {@link SerializerOutputFunction} in the output class, which must be public and static.
   *
   * @param outputClass the class of the output
   * @param <T>         the type of the output
   *
   * @return the serializer
   *
   * @throws IllegalArgumentException if the output class does not have the required methods
   */
  public <T extends O> Serializer<I, O> register(Class<T> outputClass) {
    Method outputMethod = null;
    Method inputMethod = null;

    for (Method method : outputClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(SerializerInputFunction.class)) {
        inputMethod = method;
      } else if (method.isAnnotationPresent(SerializerOutputFunction.class)) {
        outputMethod = method;
      }
    }

    if (outputMethod == null) {
      throw new IllegalArgumentException("Cannot find the output method");
    }
    if (outputMethod.getParameterCount() != 1) {
      throw new IllegalArgumentException("The output method must have 1 parameter");
    }
    if (!outputClass.isAssignableFrom(outputMethod.getReturnType())) {
      throw new IllegalArgumentException("The output method must return a type that is an instance of " + outputClass.getName());
    }
    if (!Modifier.isPublic(outputMethod.getModifiers())) {
      throw new IllegalArgumentException("The output method must be public");
    }
    if (!Modifier.isStatic(outputMethod.getModifiers())) {
      throw new IllegalArgumentException("The output method must be static");
    }

    if (inputMethod == null) {
      throw new IllegalArgumentException("Cannot find the input method");
    }
    if (inputMethod.getParameterCount() != 0) {
      throw new IllegalArgumentException("The input method must have 0 parameter");
    }
    if (!Modifier.isPublic(inputMethod.getModifiers())) {
      throw new IllegalArgumentException("The input method must be public");
    }
    if (Modifier.isStatic(inputMethod.getModifiers())) {
      throw new IllegalArgumentException("The input method must not be static");
    }

    Method finalOutputMethod = outputMethod;
    Method finalInputMethod = inputMethod;

    Function<I, T> outputFunction = input -> {
      try {
        return outputClass.cast(finalOutputMethod.invoke(null, input));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };
    Function<T, I> inputFunction = output -> {
      try {
        //noinspection unchecked
        return (I) finalInputMethod.invoke(output);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };

    return register(outputClass, outputFunction, inputFunction);
  }

  /**
   * Unregister an entry
   *
   * @param type the type
   *
   * @return the serializer
   */
  public Serializer<I, O> unregister(String type) {
    entryList.removeIf(entry -> entry.type.equals(type));
    return this;
  }

  /**
   * Unregister an entry
   *
   * @param outputClass the class of the output
   *
   * @return the serializer
   */
  public Serializer<I, O> unregister(Class<? extends O> outputClass) {
    entryList.removeIf(entry -> entry.outputClass.equals(outputClass));
    return this;
  }

  /**
   * Deserialize the input
   *
   * @param type  the type
   * @param input the input
   *
   * @return the output
   */
  public O deserialize(String type, I input) {
    return entryList.stream()
      .filter(entry -> entry.type.equals(type))
      .findFirst()
      .map(entry -> entry.outputFunction.apply(input))
      .orElseThrow(() -> new IllegalArgumentException("Cannot find the type: " + type));
  }

  /**
   * Serialize the output
   *
   * @param output the output
   *
   * @return the entry of the type and the input
   */
  public Map.Entry<String, I> serialize(O output) {
    return entryList.stream()
      .filter(entry -> entry.outputClass.isInstance(output))
      .findFirst()
      .map(entry -> new AbstractMap.SimpleEntry<>(entry.type, entry.inputFunction.apply(output)))
      .orElseThrow(() -> new IllegalArgumentException("Cannot find the type for class: " + output.getClass().getName()));
  }

  /**
   * Get the registered types
   *
   * @return the registered types
   */
  public Map<String, Class<? extends O>> getRegisteredTypes() {
    return entryList.stream().collect(Collectors.toMap(entry -> entry.type, entry -> entry.outputClass, (a, b) -> a));
  }

  private static class Entry<I, O> {
    private final String type;
    private final Class<? extends O> outputClass;
    private final Function<I, O> outputFunction;
    private final Function<O, I> inputFunction;

    private Entry(String type, Class<? extends O> outputClass, Function<I, O> outputFunction, Function<O, I> inputFunction) {
      this.type = type;
      this.outputClass = outputClass;
      this.outputFunction = outputFunction;
      this.inputFunction = inputFunction;
    }
  }
}
