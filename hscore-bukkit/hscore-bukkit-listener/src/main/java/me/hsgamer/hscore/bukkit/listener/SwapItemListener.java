package me.hsgamer.hscore.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * A listener to catch the event when a player swaps two items in the inventory
 *
 * @param <P> the plugin
 */
public abstract class SwapItemListener<P extends Plugin> extends BaseListener<P> {
  /**
   * Create a new listener
   *
   * @param plugin the plugin
   */
  protected SwapItemListener(P plugin) {
    super(plugin);
  }

  @Override
  public List<ListenerExecutor<? extends Event>> getExecutors() {
    return Collections.singletonList(new ListenerExecutor<>(InventoryClickEvent.class, this::onSwap));
  }

  /**
   * Called when a player swaps two items in the inventory
   *
   * @param player the player
   * @param target the target item
   * @param cursor the cursor item
   * @param action the inventory action
   *
   * @return the result
   */
  @NotNull
  protected abstract SwapItemListener.Result onSwap(
    @NotNull Player player,
    @NotNull ItemStack target,
    @NotNull ItemStack cursor,
    @NotNull InventoryAction action
  );

  private void onSwap(InventoryClickEvent event) {
    InventoryAction action = event.getAction();
    if (!action.equals(InventoryAction.SWAP_WITH_CURSOR) && !action.name().startsWith("PLACE_")) return;
    ItemStack target = event.getCurrentItem();
    ItemStack cursor = event.getCursor();
    if (target == null || cursor == null) return;

    Player player = (Player) event.getWhoClicked();
    Result result = onSwap(player, target, cursor, action);
    if (!result.success) return;
    event.setCancelled(true);
    event.setCurrentItem(result.target);
    player.setItemOnCursor(result.cursor);
  }

  /**
   * The result of the swap event
   */
  public static class Result {
    /**
     * Whether the event is successful.
     * If it is true, the event will be cancelled and the items will be changed.
     */
    public final boolean success;
    /**
     * The new target item.
     * If it is null, the target item will be removed.
     */
    @Nullable
    public final ItemStack target;
    /**
     * The new cursor item.
     * If it is null, the cursor item will be removed.
     */
    @Nullable
    public final ItemStack cursor;

    /**
     * Create a new result
     *
     * @param success whether the event is successful
     * @param target  the new target item
     * @param cursor  the new cursor item
     */
    public Result(boolean success, @Nullable ItemStack target, @Nullable ItemStack cursor) {
      this.success = success;
      this.target = target;
      this.cursor = cursor;
    }
  }
}
