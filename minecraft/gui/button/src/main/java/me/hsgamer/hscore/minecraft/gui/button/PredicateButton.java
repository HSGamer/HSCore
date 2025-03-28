package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The button with predicates
 */
public class PredicateButton implements GUIElement, Function<@NotNull InventoryContext, @Nullable ActionItem> {
  private final Set<UUID> clickCheckList = new ConcurrentSkipListSet<>();

  private Function<@NotNull InventoryContext, @Nullable ActionItem> button = context -> null;
  private Function<@NotNull InventoryContext, @Nullable ActionItem> fallbackButton = context -> null;
  private Predicate<UUID> viewPredicate = uuid -> true;
  private Function<ClickEvent, CompletableFuture<Boolean>> clickFuturePredicate = clickEvent -> CompletableFuture.completedFuture(true);
  private boolean preventSpamClick = false;

  /**
   * Set the view predicate
   *
   * @param viewPredicate the view predicate
   */
  public void setViewPredicate(@NotNull Predicate<@NotNull UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
  }

  /**
   * Set the click predicate
   *
   * @param clickPredicate the click predicate
   */
  public void setClickPredicate(@NotNull Predicate<@NotNull ClickEvent> clickPredicate) {
    this.clickFuturePredicate = clickEvent -> CompletableFuture.supplyAsync(() -> clickPredicate.test(clickEvent));
  }

  /**
   * Set the click future predicate
   *
   * @param clickFuturePredicate the click future predicate
   */
  public void setClickFuturePredicate(@NotNull Function<@NotNull ClickEvent, @NotNull CompletableFuture<@NotNull Boolean>> clickFuturePredicate) {
    this.clickFuturePredicate = clickFuturePredicate;
  }

  /**
   * Set whether to prevent spam click when checking click predicate
   *
   * @param preventSpamClick true if it should
   */
  public void setPreventSpamClick(boolean preventSpamClick) {
    this.preventSpamClick = preventSpamClick;
  }

  /**
   * Get the button
   *
   * @return the button
   */
  public Function<@NotNull InventoryContext, @Nullable ActionItem> getButton() {
    return button;
  }

  /**
   * Set the button
   *
   * @param button the button
   */
  public void setButton(@NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> button) {
    this.button = button;
  }

  /**
   * Get the fallback button
   *
   * @return the fallback button
   */
  public Function<@NotNull InventoryContext, @Nullable ActionItem> getFallbackButton() {
    return fallbackButton;
  }

  /**
   * Set the fallback button
   *
   * @param fallbackButton the fallback button
   */
  public void setFallbackButton(@NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> fallbackButton) {
    this.fallbackButton = fallbackButton;
  }

  @Override
  public @Nullable ActionItem apply(@NotNull InventoryContext context) {
    UUID uuid = context.getViewerID();
    ActionItem actionItem;
    if (viewPredicate.test(uuid)) {
      actionItem = button.apply(context);
    } else {
      actionItem = fallbackButton.apply(context);
    }

    if (actionItem == null) {
      return null;
    }

    return new ActionItem().apply(actionItem).extendAction(ClickEvent.class, (event, oldAction) -> {
      if (preventSpamClick && clickCheckList.contains(uuid)) {
        return;
      }
      clickCheckList.add(uuid);
      clickFuturePredicate.apply(event).thenAccept(result -> {
        clickCheckList.remove(uuid);
        if (Boolean.TRUE.equals(result)) {
          oldAction.accept(event);
        }
      });
    });
  }

  @Override
  public void init() {
    GUIElement.handleIfElement(button, GUIElement::init);
    GUIElement.handleIfElement(fallbackButton, GUIElement::init);
  }

  @Override
  public void stop() {
    GUIElement.handleIfElement(button, GUIElement::stop);
    GUIElement.handleIfElement(fallbackButton, GUIElement::stop);
  }
}
