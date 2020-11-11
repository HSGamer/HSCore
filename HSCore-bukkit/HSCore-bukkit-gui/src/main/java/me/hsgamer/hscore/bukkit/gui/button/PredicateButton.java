package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.bukkit.gui.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.BiPredicate;

/**
 * The button with click predicate
 */
public class PredicateButton implements Button {

  private final Button button;
  private final BiPredicate<UUID, InventoryClickEvent> predicate;

  /**
   * Create a predicate button
   *
   * @param button    the original button
   * @param predicate the click predicate
   */
  public PredicateButton(Button button, BiPredicate<UUID, InventoryClickEvent> predicate) {
    this.button = button;
    this.predicate = predicate;
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    return button.getItemStack(uuid);
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    if (predicate.test(uuid, event)) {
      button.handleAction(uuid, event);
    }
  }

  @Override
  public void init() {
    button.init();
  }

  @Override
  public void stop() {
    button.stop();
  }
}
