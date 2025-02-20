package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.bukkit.gui.common.event.BukkitClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.action.Action;
import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
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
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * The button that stores the output button for the viewer
 */
public class OutputButton implements Button {
  private final Map<UUID, ItemStack> map = new IdentityHashMap<>();
  private BiFunction<@NotNull UUID, @Nullable ItemStack, @Nullable ItemStack> displayItemFunction = (uuid, item) -> item;

  @Override
  public @Nullable ActionItem getItem(InventoryContext context) {
    UUID uuid = context.getViewerID();
    return new ActionItem()
      .setItem(displayItemFunction.apply(uuid, getOutputItem(uuid)))
      .setAction(new Action() {
        @Override
        public void handleClick(ClickEvent event) {
          if (!(event instanceof BukkitClickEvent)) return;
          UUID viewerID = event.getViewerID();
          InventoryClickEvent bukkitEvent = ((BukkitClickEvent) event).getEvent();
          ItemStack item = bukkitEvent.getCursor();
          if (item != null && item.getType() != Material.AIR) {
            return;
          }
          ItemStack storeItem = getOutputItem(viewerID);
          bukkitEvent.getWhoClicked().setItemOnCursor(storeItem);
          setOutputItem(viewerID, null);
        }
      });
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
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public OutputButton setDisplayItemFunction(@NotNull BiFunction<UUID, ItemStack, ItemStack> displayItemFunction) {
    this.displayItemFunction = displayItemFunction;
    return this;
  }
}
