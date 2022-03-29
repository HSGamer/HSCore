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
  public static void giveItem(Player player, ItemStack itemStack) {
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
   */
  public static List<ItemStack> getMatchedItemsInInventory(Inventory inventory, Predicate<ItemStack> predicate, int maxAmount) {
    List<ItemStack> list = new ArrayList<>();
    int amountToCheck = maxAmount;
    for (ItemStack i : inventory.getContents()) {
      if (i == null) continue;
      if (predicate.test(i)) {
        if (i.getAmount() >= amountToCheck) {
          ItemStack item = i.clone();
          item.setAmount(amountToCheck);
          list.add(item);
          break;
        } else {
          list.add(i);
          amountToCheck -= i.getAmount();
        }
      }
    }
    return list;
  }

  /**
   * Remove the items in the inventory
   *
   * @param inventory the inventory
   * @param items     the items
   */
  public static void removeItemInInventory(Inventory inventory, List<ItemStack> items) {
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
   */
  public static void removeItemInInventory(Inventory inventory, Predicate<ItemStack> predicate, int amount) {
    List<ItemStack> list = getMatchedItemsInInventory(inventory, predicate, amount);
    removeItemInInventory(inventory, list);
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
}
