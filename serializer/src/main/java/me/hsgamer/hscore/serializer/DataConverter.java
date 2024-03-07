package me.hsgamer.hscore.serializer;

import java.util.Map;

/**
 * The data converter, which converts the raw object to the final object and vice versa
 *
 * @param <F> the type of the raw object
 * @param <T> the type of the final object
 * @param <I> the type of the input of the serializer
 * @param <O> the type of the output of the serializer
 */
public abstract class DataConverter<F, T, I, O> {
  private final Serializer<I, O> serializer;

  /**
   * Create a new data converter
   */
  protected DataConverter() {
    this.serializer = createSerializer();
  }

  /**
   * Create the serializer
   *
   * @return the serializer
   */
  protected abstract Serializer<I, O> createSerializer();

  /**
   * Convert the data
   *
   * @param data the data
   * @param from the object to convert from
   *
   * @return the final object
   */
  protected abstract T convertTo(O data, F from);

  /**
   * Convert the data
   *
   * @param data the data
   * @param type the type of the data
   * @param to   the final object
   *
   * @return the raw object
   */
  protected abstract F convertFrom(I data, String type, T to);

  /**
   * Get the output from the final object
   *
   * @param to the final object
   *
   * @return the output
   */
  protected abstract O getOutput(T to);

  /**
   * Get the input from the raw object
   *
   * @param from the raw object
   *
   * @return the input
   */
  protected abstract I getInput(F from);

  /**
   * Get the type of the raw object
   *
   * @param from the raw object
   *
   * @return the type
   */
  protected abstract String getType(F from);

  /**
   * Deserialize the raw object
   *
   * @param from the raw object
   *
   * @return the final object
   */
  public T deserialize(F from) {
    I input = getInput(from);
    String type = getType(from);
    O data = serializer.deserialize(type, input);
    return convertTo(data, from);
  }

  /**
   * Serialize the final object
   *
   * @param to the final object
   *
   * @return the raw object
   */
  public F serialize(T to) {
    O output = getOutput(to);
    Map.Entry<String, I> entry = serializer.serialize(output);
    return convertFrom(entry.getValue(), entry.getKey(), to);
  }

  /**
   * Get the serializer
   *
   * @return the serializer
   */
  public Serializer<I, O> getSerializer() {
    return serializer;
  }
}
