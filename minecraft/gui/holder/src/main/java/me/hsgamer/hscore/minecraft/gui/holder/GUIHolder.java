package me.hsgamer.hscore.minecraft.gui.holder;

import me.hsgamer.hscore.minecraft.gui.common.action.Action;
import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.element.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.event.DragEvent;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import me.hsgamer.hscore.minecraft.gui.holder.event.CloseEvent;
import me.hsgamer.hscore.minecraft.gui.holder.event.OpenEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class GUIHolder<T extends InventoryContext> implements GUIElement, Action {
  private final UUID viewerID;
  private final ButtonMap buttonMap;
  private final AtomicReference<Map<Integer, ActionItem>> itemMapRef = new AtomicReference<>(null);
  private T inventoryContext;

  public GUIHolder(UUID viewerID, ButtonMap buttonMap) {
    this.viewerID = viewerID;
    this.buttonMap = buttonMap;
  }

  protected abstract T createInventoryContext(UUID uuid);

  protected abstract void setItem(int slot, @Nullable Object item);

  public abstract void open(UUID uuid);

  public void handleOpen(OpenEvent event) {

  }

  public void handleClose(CloseEvent event) {

  }

  @Override
  public void handleClick(ClickEvent event) {
    getItem(event.getSlot())
      .map(ActionItem::getAction)
      .ifPresent(action -> action.handleClick(event));
  }

  @Override
  public void handleDrag(DragEvent event) {
    for (int slot : event.getSlots()) {
      getItem(slot)
        .map(ActionItem::getAction)
        .ifPresent(action -> action.handleDrag(event));
    }
  }

  public void update() {
    itemMapRef.accumulateAndGet(buttonMap.getItemMap(inventoryContext), (oldMap, newMap) -> {
      Collection<Integer> removedSlots = null;

      if (newMap == null) {
        if (oldMap != null) {
          removedSlots = oldMap.keySet();
        }
      } else {
        if (oldMap != null) {
          removedSlots = oldMap.keySet().stream()
            .filter(slot -> !newMap.containsKey(slot))
            .collect(Collectors.toList());
        }
        newMap.forEach(this::setItem);
      }

      if (removedSlots != null) {
        removedSlots.forEach(slot -> setItem(slot, null));
      }

      return newMap;
    });
  }

  public void open() {
    open(inventoryContext.getViewerID());
  }

  @Override
  public void init() {
    inventoryContext = createInventoryContext(viewerID);
    update();
  }

  @Override
  public void stop() {
    inventoryContext = null;
    itemMapRef.set(null);
  }

  public UUID getViewerID() {
    return viewerID;
  }

  public T getInventoryContext() {
    if (inventoryContext == null) {
      throw new IllegalStateException("InventoryContext is not initialized");
    }
    return inventoryContext;
  }

  public Optional<ActionItem> getItem(int slot) {
    return Optional.ofNullable(itemMapRef.get()).map(map -> map.get(slot));
  }

  public Map<Integer, ActionItem> getItemMap() {
    return Optional.ofNullable(itemMapRef.get()).map(Collections::unmodifiableMap).orElseGet(Collections::emptyMap);
  }
}
