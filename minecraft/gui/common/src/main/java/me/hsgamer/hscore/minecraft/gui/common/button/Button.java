package me.hsgamer.hscore.minecraft.gui.common.button;

import me.hsgamer.hscore.minecraft.gui.common.element.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.Nullable;

public interface Button extends GUIElement {
  Button EMPTY = context -> null;

  @Nullable ActionItem getItem(InventoryContext context);
}
