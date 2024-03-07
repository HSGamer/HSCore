package me.hsgamer.hscore.serializer;

import me.hsgamer.hscore.serializer.annotation.SerializerInputFunction;
import me.hsgamer.hscore.serializer.annotation.SerializerOutputFunction;
import me.hsgamer.hscore.serializer.annotation.SerializerType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The serializer
 *
 * @param <I> the type of the input object
 * @param <O> the type of the output object
 */
public class Serializer<I, O> {
  private final Map<String, Entry<I, O>> typeEntryMap = new HashMap<>();
  private final Map<Class<? extends O>, Entry<I, O>> classEntryMap = new IdentityHashMap<>();

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
   *
   * @throws IllegalArgumentException if the type or the class is already registered
   */
  public <T extends O> Serializer<I, O> register(String type, Class<T> outputClass, Function<I, T> outputFunction, Function<T, I> inputFunction) {
    if (typeEntryMap.containsKey(type)) {
      throw new IllegalArgumentException("The type is already registered: " + type);
    }
    if (classEntryMap.containsKey(outputClass)) {
      throw new IllegalArgumentException("The class is already registered: " + outputClass.getName());
    }

    Entry<I, O> entry = new Entry<>(type, outputClass, outputFunction::apply, input -> inputFunction.apply(outputClass.cast(input)));
    typeEntryMap.put(type, entry);
    classEntryMap.put(outputClass, entry);
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
    Entry<I, O> entry = typeEntryMap.remove(type);
    if (entry != null) {
      classEntryMap.remove(entry.outputClass);
    }
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
    Entry<I, O> entry = classEntryMap.remove(outputClass);
    if (entry != null) {
      typeEntryMap.remove(entry.type);
    }
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
    Entry<I, O> entry = typeEntryMap.get(type);
    if (entry == null) {
      throw new IllegalArgumentException("Cannot find the type: " + type);
    }
    return entry.outputFunction.apply(input);
  }

  /**
   * Serialize the output
   *
   * @param output the output
   *
   * @return the entry of the type and the input
   */
  public Map.Entry<String, I> serialize(O output) {
    Class<?> outputClass = output.getClass();
    Entry<I, O> entry = classEntryMap.get(outputClass);
    if (entry == null) {
      throw new IllegalArgumentException("Cannot find the type for class: " + output.getClass().getName());
    }
    return new AbstractMap.SimpleEntry<>(entry.type, entry.inputFunction.apply(output));
  }

  /**
   * Get the registered types
   *
   * @return the registered types
   */
  public Map<String, Class<? extends O>> getRegisteredTypes() {
    return Collections.unmodifiableMap(typeEntryMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().outputClass)));
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
