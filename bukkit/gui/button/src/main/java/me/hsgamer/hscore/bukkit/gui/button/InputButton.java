package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.bukkit.gui.common.event.BukkitClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
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
import java.util.function.Function;

/**
 * The button that stores the input item
 */
public class InputButton implements GUIElement, Function<@NotNull InventoryContext, @Nullable ActionItem> {
  private final Map<UUID, ItemStack> map = new IdentityHashMap<>();
  private BiFunction<@NotNull UUID, @Nullable ItemStack, @Nullable ItemStack> displayItemFunction = (uuid, item) -> item;

  @Override
  public @Nullable ActionItem apply(@NotNull InventoryContext context) {
    UUID uuid = context.getViewerID();
    return new ActionItem()
      .setItem(displayItemFunction.apply(uuid, getInputItem(uuid)))
      .setAction(BukkitClickEvent.class, event -> {
        InventoryClickEvent bukkitEvent = event.getEvent();
        ItemStack cursorItem = Optional.ofNullable(bukkitEvent.getCursor())
          .filter(itemStack -> itemStack.getType() != Material.AIR)
          .map(ItemStack::clone)
          .orElse(null);
        ItemStack storeItem = getInputItem(uuid);
        bukkitEvent.getWhoClicked().setItemOnCursor(storeItem);
        setInputItem(uuid, cursorItem);
      });
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
