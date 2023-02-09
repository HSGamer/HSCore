package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minecraft.gui.GUIHolder;
import me.hsgamer.hscore.minecraft.gui.GUIProperties;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * The {@link GUIHolder} for Minestom
 */
public class MinestomGUIHolder extends GUIHolder<MinestomGUIDisplay> {
  static {
    GUIProperties.setMillisPerTick(MinecraftServer.TICK_MS);
  }

  private InventoryType inventoryType = InventoryType.CHEST_3_ROW;
  private Function<UUID, Component> titleFunction = uuid -> Component.empty();

  /**
   * Get the inventory type
   *
   * @return the inventory type
   */
  @NotNull
  public InventoryType getInventoryType() {
    return inventoryType;
  }

  /**
   * Set the inventory type
   *
   * @param inventoryType the inventory type
   */
  public void setInventoryType(@NotNull InventoryType inventoryType) {
    this.inventoryType = inventoryType;
  }

  /**
   * Get the title function
   *
   * @return the title function
   */
  @NotNull
  public Function<@NotNull UUID, @NotNull Component> getTitleFunction() {
    return titleFunction;
  }

  /**
   * Set the title function
   *
   * @param titleFunction the title function
   */
  public void setTitleFunction(Function<@NotNull UUID, @NotNull Component> titleFunction) {
    this.titleFunction = titleFunction;
  }

  /**
   * Get the title for the unique id
   *
   * @param uuid the unique id
   *
   * @return the title
   *
   * @see #getTitleFunction()
   */
  @NotNull
  public Component getTitle(@NotNull UUID uuid) {
    return titleFunction.apply(uuid);
  }

  /**
   * Set the title
   *
   * @param title the title
   *
   * @see #setTitleFunction(Function)
   */
  public void setTitle(@NotNull Component title) {
    setTitleFunction(uuid -> title);
  }

  @Override
  protected @NotNull MinestomGUIDisplay newDisplay(UUID uuid) {
    return new MinestomGUIDisplay(uuid, this);
  }

  @Override
  protected void closeAll(List<MinestomGUIDisplay> displays) {
    displays.stream()
      .map(MinestomGUIDisplay::getInventory)
      .filter(Objects::nonNull)
      .forEach(inventory -> inventory.getViewers().forEach(Player::closeInventory));
  }
}
