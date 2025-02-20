package me.hsgamer.hscore.minecraft.gui.common.button;

import me.hsgamer.hscore.minecraft.gui.common.element.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ButtonMap extends GUIElement {
  ButtonMap EMPTY = context -> null;

  @Nullable Map<Integer, ActionItem> getItemMap(InventoryContext context);
}
