package me.hsgamer.hscore.bukkit.clicktype;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

/**
 * The utility to work with AdvancedClickType
 */
public final class ClickTypeUtils {
  private static final Map<String, BukkitClickType> clickTypeMap = new HashMap<>();

  static {
    List<BukkitClickType> clickTypeList = new ArrayList<>();

    // The original click types
    for (ClickType clickType : ClickType.values()) {
      clickTypeList.add(new BukkitClickType(clickType));
    }

    // NUMBER_KEY with hotbar
    IntStream
      .range(0, 9)
      .mapToObj(i -> new BukkitClickType(ClickType.NUMBER_KEY, i))
      .forEach(clickTypeList::add);

    clickTypeList.forEach(clickType -> clickTypeMap.put(clickType.getName(), clickType));
  }

  private ClickTypeUtils() {

  }

  /**
   * Get click type from the click event
   *
   * @param event   the event
   * @param useSlot will we also the slot
   *
   * @return the click type
   */
  @NotNull
  public static BukkitClickType getClickTypeFromEvent(@NotNull final InventoryClickEvent event, final boolean useSlot) {
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
  public static Map<String, BukkitClickType> getClickTypeMap() {
    return Collections.unmodifiableMap(clickTypeMap);
  }
}
