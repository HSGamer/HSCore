package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.DisplayButton;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The button with predicates
 */
public class PredicateButton implements Button {
  private final Set<UUID> clickCheckList = new ConcurrentSkipListSet<>();

  private Button button = EMPTY;
  private Button fallbackButton = EMPTY;
  private Predicate<UUID> viewPredicate = uuid -> true;
  private Function<ClickEvent, CompletableFuture<Boolean>> clickFuturePredicate = clickEvent -> CompletableFuture.completedFuture(true);
  private boolean preventSpamClick = false;

  /**
   * Set the view predicate
   *
   * @param viewPredicate the view predicate
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateButton setViewPredicate(@NotNull Predicate<@NotNull UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
    return this;
  }

  /**
   * Set the click predicate
   *
   * @param clickPredicate the click predicate
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateButton setClickPredicate(@NotNull Predicate<@NotNull ClickEvent> clickPredicate) {
    this.clickFuturePredicate = clickEvent -> CompletableFuture.supplyAsync(() -> clickPredicate.test(clickEvent));
    return this;
  }

  /**
   * Set the click future predicate
   *
   * @param clickFuturePredicate the click future predicate
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateButton setClickFuturePredicate(@NotNull Function<@NotNull ClickEvent, @NotNull CompletableFuture<@NotNull Boolean>> clickFuturePredicate) {
    this.clickFuturePredicate = clickFuturePredicate;
    return this;
  }

  /**
   * Set whether to prevent spam click when checking click predicate
   *
   * @param preventSpamClick true if it should
   *
   * @return this instance
   */
  @Contract("_ -> this")
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
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateButton setButton(@NotNull Button button) {
    this.button = button;
    return this;
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
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateButton setFallbackButton(@NotNull Button fallbackButton) {
    this.fallbackButton = fallbackButton;
    return this;
  }

  @Override
  public DisplayButton display(@NotNull UUID uuid) {
    DisplayButton displayButton;
    if (viewPredicate.test(uuid)) {
      displayButton = button.display(uuid);
    } else {
      displayButton = fallbackButton.display(uuid);
    }

    if (displayButton == null) {
      return null;
    }

    Optional.ofNullable(displayButton.getAction())
      .map(displayButtonAction -> (Consumer<ViewerEvent>) event -> {
        if (event instanceof ClickEvent) {
          ClickEvent clickEvent = (ClickEvent) event;
          if (preventSpamClick && clickCheckList.contains(uuid)) {
            return;
          }
          clickCheckList.add(uuid);
          clickFuturePredicate.apply(clickEvent).thenAccept(result -> {
            clickCheckList.remove(uuid);
            if (Boolean.TRUE.equals(result)) {
              displayButtonAction.accept(event);
            }
          });
        } else {
          displayButtonAction.accept(event);
        }
      })
      .ifPresent(displayButton::setAction);

    return displayButton;
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
