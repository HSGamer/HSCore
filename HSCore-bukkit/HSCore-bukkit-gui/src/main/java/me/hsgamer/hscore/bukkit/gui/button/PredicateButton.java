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
  private final Button fallbackButton;

  /**
   * Create a predicate button
   *
   * @param button         the original button
   * @param fallbackButton the button used when the unique id fails the predicate
   * @param viewPredicate  the view predicate
   * @param clickPredicate the click predicate
   */
  public PredicateButton(Button button, Button fallbackButton, Predicate<UUID> viewPredicate, BiPredicate<UUID, InventoryClickEvent> clickPredicate) {
    this.button = button;
    this.fallbackButton = fallbackButton;
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
    this(button, Button.EMPTY, uuid -> true, clickPredicate);
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    if (viewPredicate.test(uuid)) {
      return button.getItemStack(uuid);
    }
    return fallbackButton.getItemStack(uuid);
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    if (viewPredicate.test(uuid) && clickPredicate.test(uuid, event)) {
      button.handleAction(uuid, event);
    } else {
      fallbackButton.handleAction(uuid, event);
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
