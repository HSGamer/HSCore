package me.hsgamer.minecraft.gui.button.impl;

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

public class PredicateButton implements Button {
  private final Button button;
  private final Set<UUID> failToViewList = new ConcurrentSkipListSet<>();
  private final Set<UUID> clickCheckList = new ConcurrentSkipListSet<>();

  private Predicate<UUID> viewPredicate = uuid -> true;
  private Function<ClickEvent, CompletableFuture<Boolean>> clickFuturePredicate = clickEvent -> CompletableFuture.completedFuture(true);
  private boolean preventSpamClick = false;
  private Button fallbackButton = EMPTY;

  public PredicateButton(Button button) {
    this.button = button;
  }

  public PredicateButton setViewPredicate(Predicate<UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
    return this;
  }

  public PredicateButton setClickPredicate(Predicate<ClickEvent> clickPredicate) {
    this.clickFuturePredicate = clickEvent -> CompletableFuture.completedFuture(clickPredicate.test(clickEvent));
    return this;
  }

  public PredicateButton setClickFuturePredicate(Function<ClickEvent, CompletableFuture<Boolean>> clickFuturePredicate) {
    this.clickFuturePredicate = clickFuturePredicate;
    return this;
  }

  public PredicateButton setPreventSpamClick(boolean preventSpamClick) {
    this.preventSpamClick = preventSpamClick;
    return this;
  }

  public Button getButton() {
    return button;
  }

  public Button getFallbackButton() {
    return fallbackButton;
  }

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
