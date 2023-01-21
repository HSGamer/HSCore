package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The button with predicates
 */
public class PredicateButton implements Button {
  private final Set<UUID> failToViewList = new ConcurrentSkipListSet<>();
  private final Set<UUID> clickCheckList = new ConcurrentSkipListSet<>();

  private Button button;
  private Button fallbackButton;
  private Predicate<UUID> viewPredicate = uuid -> true;
  private Function<ClickEvent, CompletableFuture<Boolean>> clickFuturePredicate = clickEvent -> CompletableFuture.completedFuture(true);
  private boolean preventSpamClick = false;

  /**
   * Create a predicate button
   *
   * @param button         the original button
   * @param fallbackButton the fallback button
   */
  public PredicateButton(Button button, Button fallbackButton) {
    this.button = button;
    this.fallbackButton = fallbackButton;
  }

  /**
   * Create a predicate button
   *
   * @param button the original button
   */
  public PredicateButton(Button button) {
    this(button, EMPTY);
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
  public PredicateButton setClickPredicate(Predicate<ClickEvent> clickPredicate) {
    this.clickFuturePredicate = clickEvent -> CompletableFuture.completedFuture(clickPredicate.test(clickEvent));
    return this;
  }

  /**
   * Set the click future predicate
   *
   * @param clickFuturePredicate the click future predicate
   *
   * @return {@code this} for builder chain
   */
  public PredicateButton setClickFuturePredicate(Function<ClickEvent, CompletableFuture<Boolean>> clickFuturePredicate) {
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
   * Set the button
   *
   * @param button the button
   */
  public void setButton(Button button) {
    this.button = button;
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
  public Item getItem(@NotNull UUID uuid) {
    if (viewPredicate.test(uuid)) {
      failToViewList.remove(uuid);
      return button.getItem(uuid);
    } else {
      failToViewList.add(uuid);
      return fallbackButton.getItem(uuid);
    }
  }

  @Override
  public void handleAction(ClickEvent event) {
    UUID uuid = event.getViewerID();
    if (failToViewList.contains(uuid)) {
      fallbackButton.handleAction(event);
      return;
    }

    if (preventSpamClick && clickCheckList.contains(uuid)) {
      return;
    }
    clickCheckList.add(uuid);
    clickFuturePredicate.apply(event).thenAccept(result -> {
      clickCheckList.remove(uuid);
      if (Boolean.TRUE.equals(result)) {
        button.handleAction(event);
      }
    });
  }

  @Override
  public boolean forceSetAction(@NotNull UUID uuid) {
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
