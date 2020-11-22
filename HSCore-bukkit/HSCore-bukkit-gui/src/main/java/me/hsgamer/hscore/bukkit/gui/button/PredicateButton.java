package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.bukkit.gui.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * The button with predicates
 */
public class PredicateButton implements Button {

  private final Button button;
  private final List<UUID> failToViewList = Collections.synchronizedList(new ArrayList<>());

  private Predicate<UUID> viewPredicate = uuid -> true;
  private BiPredicate<UUID, InventoryClickEvent> clickPredicate = (uuid, inventoryClickEvent) -> true;
  private Button fallbackButton = Button.EMPTY;

  /**
   * Create a predicate button
   *
   * @param button the original button
   */
  public PredicateButton(Button button) {
    this.button = button;
  }

  /**
   * Set the view predicate
   *
   * @param viewPredicate the view predicate
   */
  public PredicateButton setViewPredicate(Predicate<UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
    return this;
  }

  /**
   * Set the click predicate
   *
   * @param clickPredicate the click predicate
   */
  public PredicateButton setClickPredicate(BiPredicate<UUID, InventoryClickEvent> clickPredicate) {
    this.clickPredicate = clickPredicate;
    return this;
  }

  /**
   * Set the fallback button
   *
   * @param fallbackButton the fallback button
   */
  public PredicateButton setFallbackButton(Button fallbackButton) {
    this.fallbackButton = fallbackButton;
    return this;
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    if (viewPredicate.test(uuid)) {
      failToViewList.remove(uuid);
      return button.getItemStack(uuid);
    } else {
      failToViewList.add(uuid);
    }
    return fallbackButton.getItemStack(uuid);
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    if (!failToViewList.contains(uuid) && clickPredicate.test(uuid, event)) {
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
