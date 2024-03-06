package me.hsgamer.hscore.task.element;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The data storage for the task
 */
public interface TaskData {
  /**
   * Create a new instance of TaskData that uses {@link ConcurrentHashMap}
   *
   * @return the new instance
   */
  static TaskData create() {
    return new TaskData() {
      private final Map<String, Object> map = new ConcurrentHashMap<>();

      @Override
      public Object put(String key, Object value) {
        return map.put(key, value);
      }

      @Override
      public Object getRaw(String key) {
        return map.get(key);
      }
    };
  }

  /**
   * Put the value to the data
   *
   * @param key   the key
   * @param value the value
   *
   * @return the previous value associated with the key or null if there is none
   */
  Object put(String key, Object value);

  /**
   * Get the raw value from the data
   *
   * @param key the key
   *
   * @return the value or null if there is none
   */
  Object getRaw(String key);

  /**
   * Get the value from the data
   *
   * @param key the key
   * @param <T> the type of the value
   *
   * @return the value or null if there is none
   *
   * @throws ClassCastException if the value is not the same type as the type of the value
   */
  default <T> T get(String key) {
    //noinspection unchecked
    return (T) getRaw(key);
  }
}
