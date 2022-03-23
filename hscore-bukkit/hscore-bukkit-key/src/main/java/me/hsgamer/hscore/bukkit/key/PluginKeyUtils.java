package me.hsgamer.hscore.bukkit.key;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

/**
 * Methods to work with {@link PluginKeyPair} conveniently
 */
public final class PluginKeyUtils {
  private PluginKeyUtils() {
    // EMPTY
  }

  /**
   * Copy the {@link PluginKeyPair} from the source container to the target container
   *
   * @param fromContainer the source container
   * @param toContainer   the target container
   * @param keyPairs      the key pairs
   */
  public static void copy(PersistentDataContainer fromContainer, PersistentDataContainer toContainer, PluginKeyPair<?, ?>... keyPairs) {
    for (PluginKeyPair<?, ?> keyPair : keyPairs) {
      keyPair.copy(fromContainer, toContainer);
    }
  }

  /**
   * Copy the {@link PluginKeyPair} from the source holder to the target holder
   *
   * @param fromHolder the source holder
   * @param toHolder   the target holder
   * @param keyPairs   the key pairs
   */
  public static void copy(PersistentDataHolder fromHolder, PersistentDataHolder toHolder, PluginKeyPair<?, ?>... keyPairs) {
    copy(fromHolder.getPersistentDataContainer(), toHolder.getPersistentDataContainer(), keyPairs);
  }
}
