package me.hsgamer.hscore.bukkit.key;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

/**
 * A pair of {@link NamespacedKey} and {@link PersistentDataType}
 *
 * @param <Z> the retrieved object type when applying this data type
 */
public class PluginKeyPair<Z> {
  private final NamespacedKey key;
  private final PersistentDataType<?, Z> dataType;
  private final Z defaultValue;

  /**
   * Create a new instance of {@link PluginKeyPair}
   *
   * @param key          the key
   * @param dataType     the data type
   * @param defaultValue the default value
   */
  public PluginKeyPair(NamespacedKey key, PersistentDataType<?, Z> dataType, Z defaultValue) {
    this.key = key;
    this.dataType = dataType;
    this.defaultValue = defaultValue;
  }

  /**
   * Get the key
   *
   * @return the key
   */
  public NamespacedKey getKey() {
    return key;
  }

  /**
   * Get the data type
   *
   * @return the data type
   */
  public PersistentDataType<?, Z> getDataType() {
    return dataType;
  }

  /**
   * Get the default value
   *
   * @return the default value
   */
  public Z getDefaultValue() {
    return defaultValue;
  }

  /**
   * Get the value from the container
   *
   * @param container the container
   *
   * @return the value
   */
  public Z get(PersistentDataContainer container) {
    return container.getOrDefault(key, dataType, defaultValue);
  }

  /**
   * Set the value to the container
   *
   * @param container the container
   * @param value     the value
   */
  public void set(PersistentDataContainer container, Z value) {
    if (value == null) {
      remove(container);
    } else {
      container.set(key, dataType, value);
    }
  }

  /**
   * Get the value from the holder
   *
   * @param holder the holder
   *
   * @return the value
   */
  public Z get(PersistentDataHolder holder) {
    return get(holder.getPersistentDataContainer());
  }

  /**
   * Set the value to the holder
   *
   * @param holder the holder
   * @param value  the value
   */
  public void set(PersistentDataHolder holder, Z value) {
    set(holder.getPersistentDataContainer(), value);
  }

  /**
   * Check if the container contains the key
   *
   * @param container the container
   */
  public boolean contains(PersistentDataContainer container) {
    return container.has(key, dataType);
  }

  /**
   * Check if the holder contains the key
   *
   * @param holder the holder
   */
  public boolean contains(PersistentDataHolder holder) {
    return contains(holder.getPersistentDataContainer());
  }

  /**
   * Copy the value from the source container to the target container
   *
   * @param fromContainer the source container
   * @param toContainer   the target container
   */
  public void copy(PersistentDataContainer fromContainer, PersistentDataContainer toContainer) {
    Z value = get(fromContainer);
    set(toContainer, value);
  }

  /**
   * Copy the value from the source holder to the target holder
   *
   * @param fromHolder the source holder
   * @param toHolder   the target holder
   */
  public void copy(PersistentDataHolder fromHolder, PersistentDataHolder toHolder) {
    copy(fromHolder.getPersistentDataContainer(), toHolder.getPersistentDataContainer());
  }

  /**
   * Remove the value from the container
   *
   * @param container the container
   */
  public void remove(PersistentDataContainer container) {
    container.remove(key);
  }

  /**
   * Remove the value from the holder
   *
   * @param holder the holder
   */
  public void remove(PersistentDataHolder holder) {
    remove(holder.getPersistentDataContainer());
  }
}
