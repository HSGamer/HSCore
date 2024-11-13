package me.hsgamer.hscore.bukkit.gui.provider;

import me.hsgamer.hscore.bukkit.gui.BukkitGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static me.hsgamer.hscore.bukkit.gui.BukkitGUIUtils.normalizeToChestSize;

/**
 * The {@link BukkitGUI} provider that uses {@link InventoryHolder} to store the GUI
 */
public class HolderBukkitGUIProvider extends BukkitGUIProvider {
  /**
   * {@inheritDoc}
   */
  public HolderBukkitGUIProvider(Plugin plugin) {
    super(plugin);
  }

  /**
   * {@inheritDoc}
   */
  public HolderBukkitGUIProvider(Plugin plugin, EventPriority clickPriority, EventPriority dragPriority, EventPriority openPriority, EventPriority closePriority) {
    super(plugin, clickPriority, dragPriority, openPriority, closePriority);
  }

  /**
   * Create a new {@link BukkitGUI}
   *
   * @param inventoryFunction the function to create the inventory
   *
   * @return the new {@link BukkitGUI}
   */
  public BukkitGUI create(Function<InventoryHolder, Inventory> inventoryFunction) {
    Holder holder = new Holder(plugin);
    Inventory inventory = inventoryFunction.apply(holder);
    BukkitGUI gui = new BukkitGUI(inventory);
    holder.gui = gui;
    return gui;
  }

  /**
   * Create a new {@link BukkitGUI}
   *
   * @param inventoryType the type of the inventory
   * @param size          the size of the inventory (only for CHEST)
   * @param title         the title of the inventory
   *
   * @return the new {@link BukkitGUI}
   */
  public BukkitGUI create(InventoryType inventoryType, int size, String title) {
    return create(holder -> {
      Inventory inventory;
      if (inventoryType == InventoryType.CHEST) {
        inventory = Bukkit.createInventory(holder, normalizeToChestSize(size), title);
      } else {
        inventory = Bukkit.createInventory(holder, inventoryType, title);
      }
      return inventory;
    });
  }

  @Override
  protected @Nullable BukkitGUI getGUI(Inventory inventory) {
    InventoryHolder inventoryHolder = inventory.getHolder();
    if (!(inventoryHolder instanceof Holder)) {
      return null;
    }
    Holder holder = (Holder) inventoryHolder;

    if (holder.plugin != this.plugin) {
      return null;
    }

    return holder.gui;
  }

  private static class Holder implements InventoryHolder {
    private final Plugin plugin;
    private BukkitGUI gui;

    private Holder(Plugin plugin) {
      this.plugin = plugin;
    }

    @Override
    public Inventory getInventory() {
      if (gui == null) {
        throw new IllegalStateException("GUIHolder is not initialized");
      }
      return gui.getInventory();
    }
  }
}
