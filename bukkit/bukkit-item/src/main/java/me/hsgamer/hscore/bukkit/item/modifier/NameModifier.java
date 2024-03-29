package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.common.StringReplacer;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The name modifier
 */
public class NameModifier implements ItemMetaModifier, ItemMetaComparator {
  private String name;

  /**
   * Set the name
   *
   * @param name the name
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public NameModifier setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer) {
    if (this.name != null) {
      meta.setDisplayName(stringReplacer.replaceOrOriginal(name, uuid));
    }
    return meta;
  }

  @Override
  public boolean loadFromItemMeta(ItemMeta meta) {
    if (meta.hasDisplayName()) {
      this.name = meta.getDisplayName();
      return true;
    }
    return false;
  }

  @Override
  public boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer) {
    if (!meta.hasDisplayName() && this.name == null) {
      return true;
    }
    String replaced = this.name == null ? "" : stringReplacer.replaceOrOriginal(this.name, uuid);
    return replaced.equals(meta.getDisplayName());
  }

  @Override
  public Object toObject() {
    return this.name;
  }

  @Override
  public void loadFromObject(Object object) {
    this.name = String.valueOf(object);
  }
}
