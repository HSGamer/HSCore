package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.bukkit.gui.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * The button with click predicate
 */
public class PredicateButton implements Button {

  private final Button button;
  private final Predicate<UUID> viewPredicate;
  private final BiPredicate<UUID, InventoryClickEvent> clickPredicate;

  /**
   * Create a predicate button
   *
   * @param button         the original button
   * @param viewPredicate  the view predicate
   * @param clickPredicate the click predicate
   */
  public PredicateButton(Button button, Predicate<UUID> viewPredicate, BiPredicate<UUID, InventoryClickEvent> clickPredicate) {
    this.button = button;
    this.viewPredicate = viewPredicate;
    this.clickPredicate = clickPredicate;
  }

  /**
   * Create a predicate button
   *
   * @param button         the original button
   * @param clickPredicate the click predicate
   */
  public PredicateButton(Button button, BiPredicate<UUID, InventoryClickEvent> clickPredicate) {
    this(button, uuid -> true, clickPredicate);
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    if (viewPredicate.test(uuid)) {
      return button.getItemStack(uuid);
    }
    return null;
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    if (viewPredicate.test(uuid) && clickPredicate.test(uuid, event)) {
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
