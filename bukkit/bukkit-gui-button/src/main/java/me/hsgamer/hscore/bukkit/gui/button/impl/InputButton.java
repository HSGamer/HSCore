package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.DisplayButton;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * The button that stores the input item
 */
public class InputButton implements Button {
  private final Map<UUID, ItemStack> map = new IdentityHashMap<>();
  private BiFunction<@NotNull UUID, @Nullable ItemStack, @Nullable ItemStack> displayItemFunction = (uuid, item) -> item;

  @Override
  public @Nullable DisplayButton display(@NotNull UUID uuid) {
    return new DisplayButton(
      new BukkitItem(displayItemFunction.apply(uuid, getInputItem(uuid))),
      this,
      event -> {
        if (!(event instanceof BukkitClickEvent)) return;
        UUID viewerID = event.getViewerID();
        InventoryClickEvent bukkitEvent = ((BukkitClickEvent) event).getEvent();
        ItemStack cursorItem = Optional.ofNullable(bukkitEvent.getCursor())
          .filter(itemStack -> itemStack.getType() != Material.AIR)
          .map(ItemStack::clone)
          .orElse(null);
        ItemStack storeItem = getInputItem(viewerID);
        bukkitEvent.getWhoClicked().setItemOnCursor(storeItem);
        setInputItem(viewerID, cursorItem);
      }
    );
  }

  @Override
  public void stop() {
    map.clear();
  }

  /**
   * Set the input item for the unique id
   *
   * @param uuid      the unique id
   * @param itemStack the item, or null to remove the input item
   */
  public void setInputItem(@NotNull UUID uuid, @Nullable ItemStack itemStack) {
    map.compute(uuid, (uuid1, item) -> itemStack);
  }

  /**
   * Get the input item for the unique id
   *
   * @param uuid the unique id
   *
   * @return the item
   */
  @Nullable
  public ItemStack getInputItem(@NotNull UUID uuid) {
    return map.get(uuid);
  }

  /**
   * Set the function to display the item on the GUI
   *
   * @param displayItemFunction the function
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public InputButton setDisplayItemFunction(@NotNull BiFunction<UUID, ItemStack, ItemStack> displayItemFunction) {
    this.displayItemFunction = displayItemFunction;
    return this;
  }
}
