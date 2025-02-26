package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * The placeholder mask <br> Used for per-user masks
 */
public class PlaceholderMask implements GUIElement, Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> {
  protected final Map<UUID, Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>>> userMasks = new ConcurrentHashMap<>();
  protected Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> defaultMask = context -> null;

  @Override
  public void init() {
    GUIElement.handleIfElement(defaultMask, GUIElement::init);
  }

  @Override
  public void stop() {
    GUIElement.handleIfElement(defaultMask, GUIElement::stop);
    this.userMasks.clear();
  }

  @Override
  public @Nullable Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    return this.userMasks.getOrDefault(context.getViewerID(), this.defaultMask).apply(context);
  }

  /**
   * Set the mask for the unique id
   *
   * @param uuid the unique id
   * @param mask the mask
   */
  public void setMask(@NotNull UUID uuid, @Nullable Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> mask) {
    if (mask == null) {
      this.userMasks.remove(uuid);
    } else {
      this.userMasks.put(uuid, mask);
    }
  }

  /**
   * Get the mask for the unique id
   *
   * @param uuid the unique id
   *
   * @return the mask
   */
  @Nullable
  public Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> getMask(@NotNull UUID uuid) {
    return this.userMasks.get(uuid);
  }

  /**
   * Get the default mask
   *
   * @return the default mask
   */
  @NotNull
  public Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> getDefaultMask() {
    return defaultMask;
  }

  /**
   * Set the default mask
   *
   * @param defaultMask the default mask
   */
  public void setDefaultMask(@NotNull Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> defaultMask) {
    this.defaultMask = defaultMask;
  }

  /**
   * Get the user-mask map
   *
   * @return the user-mask map
   */
  @NotNull
  public Map<@NotNull UUID, @NotNull Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>>> getUserMasks() {
    return Collections.unmodifiableMap(this.userMasks);
  }
}
