package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * The button that stores the output button for the viewer
 */
public class OutputButton implements Button {
  private final Map<UUID, ItemStack> map = new IdentityHashMap<>();
  private Function<@NotNull UUID, @Nullable ItemStack> defaultItemFunction = uuid -> null;

  @Override
  public ItemStack getItemStack(UUID uuid) {
    return Optional.ofNullable(map.get(uuid)).orElseGet(() -> defaultItemFunction.apply(uuid));
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    ItemStack item = event.getCursor();
    if (item != null && item.getType() != Material.AIR) {
      return;
    }
    ItemStack storeItem = map.remove(uuid);
    event.getWhoClicked().setItemOnCursor(storeItem);
  }

  @Override
  public boolean forceSetAction(UUID uuid) {
    return true;
  }

  @Override
  public void stop() {
    map.clear();
  }

  /**
   * Set the output item for the unique id
   *
   * @param uuid      the unique id
   * @param itemStack the item, or null to remove the output button
   */
  public void setOutputItem(@NotNull UUID uuid, @Nullable ItemStack itemStack) {
    map.compute(uuid, (uuid1, item) -> itemStack);
  }

  /**
   * Get the output item for the unique id
   *
   * @param uuid the unique id
   *
   * @return the item
   */
  @Nullable
  public ItemStack getOutputItem(@NotNull UUID uuid) {
    return map.get(uuid);
  }

  /**
   * Set the function to display the item on the GUI
   *
   * @param defaultItemFunction the function
   */
  public void setDefaultItemFunction(@NotNull Function<UUID, ItemStack> defaultItemFunction) {
    this.defaultItemFunction = defaultItemFunction;
  }
}
