package me.hsgamer.hscore.bukkit.clicktype;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * The utility to work with AdvancedClickType
 */
public final class ClickTypeUtils {
  private static final Map<String, AdvancedClickType> clickTypeMap = new HashMap<>();

  static {
    // The original click types
    for (ClickType clickType : ClickType.values()) {
      clickTypeMap.put(clickType.name(), new AdvancedClickType(clickType));
    }

    // NUMBER_KEY with hotbar
    IntStream
      .range(0, 9)
      .forEach(
        i -> clickTypeMap.put(ClickType.NUMBER_KEY.name() + "_" + i, new AdvancedClickType(ClickType.NUMBER_KEY, i))
      );
  }

  private ClickTypeUtils() {

  }

  /**
   * Get click type from the click event
   *
   * @param event   the event
   * @param useSlot will we also the slot
   * @return the click type
   */
  @NotNull
  public static AdvancedClickType getClickTypeFromEvent(@NotNull final InventoryClickEvent event, final boolean useSlot) {
    ClickType clickType = event.getClick();
    if (!useSlot || !clickType.equals(ClickType.NUMBER_KEY)) {
      return clickTypeMap.get(clickType.name());
    }
    return clickTypeMap.get(ClickType.NUMBER_KEY.name() + "_" + event.getHotbarButton());
  }

  /**
   * Get the available click types
   *
   * @return the unmodifiable map
   */
  @NotNull
  public static Map<String, AdvancedClickType> getClickTypeMap() {
    return Collections.unmodifiableMap(clickTypeMap);
  }
}
