package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * The button with predicates
 */
public class PredicateButton implements Button {
  private final Button button;
  private final Set<UUID> failToViewList = new ConcurrentSkipListSet<>();
  private final Set<UUID> clickCheckList = new ConcurrentSkipListSet<>();

  private Predicate<UUID> viewPredicate = uuid -> true;
  private BiFunction<UUID, InventoryClickEvent, CompletableFuture<Boolean>> clickFuturePredicate = (uuid, inventoryClickEvent) -> CompletableFuture.completedFuture(true);
  private boolean preventSpamClick = false;
  private Button fallbackButton = EMPTY;

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
   *
   * @return {@code this} for builder chain
   */
  public PredicateButton setViewPredicate(Predicate<UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
    return this;
  }

  /**
   * Set the click predicate
   *
   * @param clickPredicate the click predicate
   *
   * @return {@code this} for builder chain
   */
  public PredicateButton setClickPredicate(BiPredicate<UUID, InventoryClickEvent> clickPredicate) {
    this.clickFuturePredicate = (uuid, inventoryClickEvent) -> CompletableFuture.completedFuture(clickPredicate.test(uuid, inventoryClickEvent));
    return this;
  }

  /**
   * Set the click future predicate
   *
   * @param clickFuturePredicate the click future predicate
   *
   * @return {@code this} for builder chain
   */
  public PredicateButton setClickFuturePredicate(BiFunction<UUID, InventoryClickEvent, CompletableFuture<Boolean>> clickFuturePredicate) {
    this.clickFuturePredicate = clickFuturePredicate;
    return this;
  }

  /**
   * Set whether to prevent spam click when checking click predicate
   *
   * @param preventSpamClick true if it should
   *
   * @return {@code this} for builder chain
   */
  public PredicateButton setPreventSpamClick(boolean preventSpamClick) {
    this.preventSpamClick = preventSpamClick;
    return this;
  }

  /**
   * Get the button
   *
   * @return the button
   */
  public Button getButton() {
    return button;
  }

  /**
   * Get the fallback button
   *
   * @return the fallback button
   */
  public Button getFallbackButton() {
    return fallbackButton;
  }

  /**
   * Set the fallback button
   *
   * @param fallbackButton the fallback button
   *
   * @return {@code this} for builder chain
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
      return fallbackButton.getItemStack(uuid);
    }
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    if (failToViewList.contains(uuid)) {
      fallbackButton.handleAction(uuid, event);
      return;
    }

    if (preventSpamClick && clickCheckList.contains(uuid)) {
      return;
    }
    clickCheckList.add(uuid);
    clickFuturePredicate.apply(uuid, event).thenAccept(result -> {
      clickCheckList.remove(uuid);
      if (Boolean.TRUE.equals(result)) {
        button.handleAction(uuid, event);
      }
    });
  }

  @Override
  public boolean forceSetAction(UUID uuid) {
    if (failToViewList.contains(uuid)) {
      return fallbackButton.forceSetAction(uuid);
    } else {
      return button.forceSetAction(uuid);
    }
  }

  @Override
  public void init() {
    button.init();
    fallbackButton.init();
  }

  @Override
  public void stop() {
    button.stop();
    fallbackButton.stop();
  }
}
