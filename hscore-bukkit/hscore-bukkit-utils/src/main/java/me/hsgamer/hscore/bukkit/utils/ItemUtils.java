package me.hsgamer.hscore.bukkit.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Methods on items
 */
public final class ItemUtils {
  private ItemUtils() {
    // EMPTY
  }

  /**
   * Give the item to the player
   *
   * @param player    the player
   * @param itemStack the item
   */
  public static void giveItem(Player player, ItemStack... itemStack) {
    player.getInventory().addItem(itemStack).values()
      .forEach(remainingItemStack -> player.getWorld().dropItem(player.getLocation(), remainingItemStack));
  }

  /**
   * Get the matched items in the inventory
   *
   * @param inventory the inventory
   * @param predicate the item predicate
   * @param maxAmount the max amount to get
   *
   * @return the matched items
   *
   * @see #createItemCheckSession(Inventory, Predicate, int)
   */
  public static List<ItemStack> getMatchedItemsInInventory(Inventory inventory, Predicate<ItemStack> predicate, int maxAmount) {
    return createItemCheckSession(inventory, predicate, maxAmount).matchedItems;
  }

  /**
   * Remove the items in the inventory
   *
   * @param inventory the inventory
   * @param items     the items
   */
  public static void removeItemInInventory(Inventory inventory, Collection<ItemStack> items) {
    for (ItemStack item : items) {
      inventory.removeItem(item);
    }
  }

  /**
   * Remove the matched items in the inventory
   *
   * @param inventory the inventory
   * @param predicate the item predicate
   * @param amount    the amount
   *
   * @see #createItemCheckSession(Inventory, Predicate, int)
   */
  public static void removeItemInInventory(Inventory inventory, Predicate<ItemStack> predicate, int amount) {
    createItemCheckSession(inventory, predicate, amount).takeRunnable.run();
  }

  /**
   * Get the predicate of the item. This will check the item similarity
   *
   * @param itemStack the item
   *
   * @return the predicate
   *
   * @see ItemStack#isSimilar(ItemStack)
   */
  public static Predicate<ItemStack> getItemPredicate(ItemStack itemStack) {
    return i -> i.isSimilar(itemStack);
  }

  /**
   * Check if the items cannot be added to the inventory
   *
   * @param inventory the inventory
   * @param items     the items
   *
   * @return true if the items cannot be added to the inventory
   */
  public static boolean isItemFullOnInventory(Inventory inventory, Collection<ItemStack> items) {
    for (ItemStack item : items) {
      if (isItemFullOnInventory(inventory, item)) return true;
    }
    return false;
  }

  /**
   * Check if the item cannot be added to the inventory
   *
   * @param inventory the inventory
   * @param item      the item
   *
   * @return true if the item cannot be added to the inventory
   */
  public static boolean isItemFullOnInventory(Inventory inventory, ItemStack item) {
    boolean canAdd = false;
    for (ItemStack i : inventory.getContents()) {
      if (i == null || i.getType() == Material.AIR) {
        canAdd = true;
      } else if (i.isSimilar(item)) {
        canAdd = i.getAmount() + item.getAmount() <= i.getMaxStackSize();
      }
      if (canAdd) {
        break;
      }
    }
    return !canAdd;
  }

  /**
   * Create a new {@link ItemCheckSession} that check the inventory for the items that match the predicate
   *
   * @param inventory the inventory
   * @param predicate the item predicate
   * @param maxAmount the max amount to check
   *
   * @return the {@link ItemCheckSession}
   *
   * @see ItemCheckSession
   */
  public static ItemCheckSession createItemCheckSession(Inventory inventory, Predicate<ItemStack> predicate, int maxAmount) {
    List<ItemStack> list = new ArrayList<>();
    List<Runnable> takeRunnable = new ArrayList<>();
    int amountToCheck = maxAmount;
    ItemStack[] contents = inventory.getContents();
    for (int slot = 0; slot < contents.length && amountToCheck <= 0; slot++) {
      ItemStack i = contents[slot];
      if (i == null || !predicate.test(i)) continue;

      if (i.getAmount() > amountToCheck) {
        int lastItemRemaining = i.getAmount() - amountToCheck;
        takeRunnable.add(() -> i.setAmount(lastItemRemaining));

        ItemStack item = i.clone();
        item.setAmount(amountToCheck);
        list.add(item);
      } else {
        int finalSlot = slot;
        takeRunnable.add(() -> inventory.setItem(finalSlot, null));

        list.add(i.clone());
      }
      amountToCheck -= Math.min(amountToCheck, i.getAmount());
    }

    return new ItemCheckSession(list, () -> {
      for (Runnable runnable : takeRunnable) {
        runnable.run();
      }
    }, amountToCheck <= 0);
  }

  /**
   * The session of the item check. Used to get the matched items and remove the items from the inventory
   */
  public static class ItemCheckSession {
    /**
     * The matched items
     */
    public final List<ItemStack> matchedItems;
    /**
     * The runnable to remove the items from the inventory
     */
    public final Runnable takeRunnable;
    /**
     * Whether all the items from the inventory are matched
     */
    public final boolean isAllMatched;

    private ItemCheckSession(List<ItemStack> matchedItems, Runnable takeRunnable, boolean isAllMatched) {
      this.matchedItems = matchedItems;
      this.takeRunnable = takeRunnable;
      this.isAllMatched = isAllMatched;
    }
  }
}
