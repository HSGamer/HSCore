package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A button that is displayed on the inventory
 */
public class DisplayButton {
  /**
   * The empty button
   */
  public static final DisplayButton EMPTY = new DisplayButton();

  private Item item;
  private Consumer<ClickEvent> clickAction;

  /**
   * Get the item to display
   *
   * @return the item
   */
  @Nullable
  public Item getItem() {
    return item;
  }

  /**
   * Set the item to display
   *
   * @param item the item
   *
   * @return the current instance
   */
  public DisplayButton setItem(@Nullable Item item) {
    this.item = item;
    return this;
  }

  /**
   * Get the click action
   *
   * @return the action
   */
  @Nullable
  public Consumer<ClickEvent> getClickAction() {
    return clickAction;
  }

  /**
   * Set the click action
   *
   * @param clickAction the action
   *
   * @return the current instance
   */
  public DisplayButton setClickAction(@Nullable Consumer<ClickEvent> clickAction) {
    this.clickAction = clickAction;
    return this;
  }
}
