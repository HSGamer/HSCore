package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The placeholder mask <br> Used for per-user masks
 */
public class PlaceholderMask implements ButtonMap {
  protected final Map<UUID, ButtonMap> userMasks = new ConcurrentHashMap<>();
  protected ButtonMap defaultMask = ButtonMap.EMPTY;
  protected boolean initDefaultMask = true;

  @Override
  public void init() {
    if (initDefaultMask) {
      this.defaultMask.init();
    }
  }

  @Override
  public void stop() {
    if (initDefaultMask) {
      this.defaultMask.stop();
    }
    this.userMasks.clear();
  }

  @Override
  public @Nullable Map<Integer, ActionItem> getItemMap(@NotNull InventoryContext context) {
    return this.userMasks.getOrDefault(context.getViewerID(), this.defaultMask).getItemMap(context);
  }

  /**
   * Set the mask for the unique id
   *
   * @param uuid the unique id
   * @param mask the mask
   */
  public void setMask(@NotNull UUID uuid, @Nullable ButtonMap mask) {
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
  public ButtonMap getMask(@NotNull UUID uuid) {
    return this.userMasks.get(uuid);
  }

  /**
   * Get the default mask
   *
   * @return the default mask
   */
  @NotNull
  public ButtonMap getDefaultMask() {
    return defaultMask;
  }

  /**
   * Set the default mask
   *
   * @param defaultMask the default mask
   */
  public void setDefaultMask(@NotNull ButtonMap defaultMask) {
    this.defaultMask = defaultMask;
  }

  /**
   * Should the default mask be initialized?
   *
   * @param initDefaultMask true if yes
   */
  public void setInitDefaultMask(boolean initDefaultMask) {
    this.initDefaultMask = initDefaultMask;
  }

  /**
   * Get the user-mask map
   *
   * @return the user-mask map
   */
  @NotNull
  public Map<@NotNull UUID, @NotNull ButtonMap> getUserMasks() {
    return Collections.unmodifiableMap(this.userMasks);
  }
}
