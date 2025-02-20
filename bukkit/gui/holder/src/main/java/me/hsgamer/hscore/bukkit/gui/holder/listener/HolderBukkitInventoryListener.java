package me.hsgamer.hscore.bukkit.gui.holder.listener;

import me.hsgamer.hscore.bukkit.gui.holder.BukkitGUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The {@link BukkitInventoryListener} to listen to inventory backed by {@link InventoryHolder}
 */
public class HolderBukkitInventoryListener extends BukkitInventoryListener {
  /**
   * {@inheritDoc}
   */
  public HolderBukkitInventoryListener(Plugin plugin) {
    super(plugin);
  }

  /**
   * {@inheritDoc}
   */
  public HolderBukkitInventoryListener(Plugin plugin, EventPriority clickPriority, EventPriority dragPriority, EventPriority openPriority, EventPriority closePriority) {
    super(plugin, clickPriority, dragPriority, openPriority, closePriority);
  }

  /**
   * Create an inventory function to create the inventory
   *
   * @param inventoryFunction the function to create the inventory with the {@link InventoryHolder}
   *
   * @return the inventory function
   */
  public BiFunction<UUID, BukkitGUIHolder, Inventory> create(BiFunction<UUID, InventoryHolder, Inventory> inventoryFunction) {
    return (uuid, guiHolder) -> {
      Holder holder = new Holder(plugin);
      Inventory inventory = inventoryFunction.apply(uuid, holder);
      holder.holder = guiHolder;
      return inventory;
    };
  }

  /**
   * Create an inventory function to create the inventory
   *
   * @param inventoryTypeFunction the function to get the inventory type
   * @param sizeFunction          the function to get the size
   * @param titleFunction         the function to get the title
   *
   * @return the inventory function
   */
  public BiFunction<UUID, BukkitGUIHolder, Inventory> create(
    Function<UUID, InventoryType> inventoryTypeFunction,
    Function<UUID, Integer> sizeFunction,
    Function<UUID, String> titleFunction
  ) {
    return create((uuid, holder) -> {
      InventoryType inventoryType = inventoryTypeFunction.apply(uuid);
      int size = sizeFunction.apply(uuid);
      String title = titleFunction.apply(uuid);
      Inventory inventory;
      if (inventoryType == InventoryType.CHEST) {
        // Normalize the size to the chest size
        int remain = size % 9;
        size -= remain;
        size += remain > 0 ? 9 : 0;

        inventory = Bukkit.createInventory(holder, size, title);
      } else {
        inventory = Bukkit.createInventory(holder, inventoryType, title);
      }
      return inventory;
    });
  }

  @Override
  protected @Nullable BukkitGUIHolder getHolder(Inventory inventory) {
    InventoryHolder inventoryHolder = inventory.getHolder();
    if (!(inventoryHolder instanceof Holder)) {
      return null;
    }
    Holder holder = (Holder) inventoryHolder;

    if (holder.plugin != this.plugin) {
      return null;
    }

    return holder.holder;
  }

  private static class Holder implements InventoryHolder {
    private final Plugin plugin;
    private BukkitGUIHolder holder;

    private Holder(Plugin plugin) {
      this.plugin = plugin;
    }

    @Override
    public Inventory getInventory() {
      if (holder == null) {
        throw new IllegalStateException("GUIHolder is not initialized");
      }
      return holder.getInventoryContext().getInventory();
    }
  }
}
