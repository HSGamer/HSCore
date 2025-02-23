package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * A base button that handles multiple child buttons
 */
public abstract class MultiButton implements GUIElement, Function<@NotNull InventoryContext, @Nullable ActionItem> {
  protected final List<Function<@NotNull InventoryContext, @Nullable ActionItem>> buttons = new ArrayList<>();

  /**
   * Whether to require child buttons
   *
   * @return true if child buttons are required
   */
  protected boolean requireChildButtons() {
    return false;
  }

  /**
   * Add child buttons
   *
   * @param buttons the child buttons
   * @param <T>     the type of the button
   */
  public final <T extends Function<@NotNull InventoryContext, @Nullable ActionItem>> void addButton(@NotNull Collection<@NotNull T> buttons) {
    this.buttons.addAll(buttons);
  }

  /**
   * Add child buttons
   *
   * @param button the button
   */
  @SafeVarargs
  public final void addButton(@NotNull Function<@NotNull InventoryContext, @Nullable ActionItem>... button) {
    addButton(Arrays.asList(button));
  }

  /**
   * Get the list of child buttons
   *
   * @return the list of child buttons
   */
  public final List<Function<@NotNull InventoryContext, @Nullable ActionItem>> getButtons() {
    return Collections.unmodifiableList(this.buttons);
  }

  @Override
  public void init() {
    if (requireChildButtons() && this.buttons.isEmpty()) {
      throw new IllegalArgumentException("There is no child button for this button");
    }
    GUIElement.handleIfElement(this.buttons, GUIElement::init);
  }

  @Override
  public void stop() {
    GUIElement.handleIfElement(this.buttons, GUIElement::stop);
  }
}
