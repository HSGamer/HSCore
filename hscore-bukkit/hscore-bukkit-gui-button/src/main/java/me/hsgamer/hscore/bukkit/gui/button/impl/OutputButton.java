package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * The button that stores the output button for the viewer
 */
public class OutputButton implements Button {
  private final Map<UUID, ItemStack> map = new IdentityHashMap<>();
  private BiFunction<@NotNull UUID, @Nullable ItemStack, @Nullable ItemStack> displayItemFunction = (uuid, item) -> item;

  @Override
  public BukkitItem getItem(@NotNull UUID uuid) {
    return new BukkitItem(displayItemFunction.apply(uuid, getOutputItem(uuid)));
  }

  @Override
  public void handleAction(@NotNull ClickEvent wrappedEvent) {
    if (!(wrappedEvent instanceof BukkitClickEvent)) return;
    UUID uuid = wrappedEvent.getViewerID();
    InventoryClickEvent event = ((BukkitClickEvent) wrappedEvent).getEvent();
    ItemStack item = event.getCursor();
    if (item != null && item.getType() != Material.AIR) {
      return;
    }
    ItemStack storeItem = getOutputItem(uuid);
    event.getWhoClicked().setItemOnCursor(storeItem);
    setOutputItem(uuid, null);
  }

  @Override
  public boolean forceSetAction(@NotNull UUID uuid) {
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
   * @param displayItemFunction the function
   */
  public void setDisplayItemFunction(@NotNull BiFunction<UUID, ItemStack, ItemStack> displayItemFunction) {
    this.displayItemFunction = displayItemFunction;
  }
}
