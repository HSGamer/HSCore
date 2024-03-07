package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.StringReplacer;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The lore modifier
 */
public class LoreModifier implements ItemMetaModifier, ItemMetaComparator {
  private final List<String> lore = new ArrayList<>();

  private List<String> getReplacedLore(UUID uuid, StringReplacer stringReplacer) {
    List<String> replacedLore = new ArrayList<>(lore);
    replacedLore.replaceAll(s -> stringReplacer.tryReplace(s, uuid));
    return replacedLore;
  }

  @Override
  public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer) {
    meta.setLore(getReplacedLore(uuid, stringReplacer));
    return meta;
  }

  @Override
  public boolean loadFromItemMeta(ItemMeta meta) {
    if (meta.hasLore()) {
      setLore(meta.getLore());
      return true;
    }
    return false;
  }

  @Override
  public boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer) {
    return (!meta.hasLore() && this.lore.isEmpty()) || getReplacedLore(uuid, stringReplacer).equals(meta.getLore());
  }

  @Override
  public Object toObject() {
    return lore;
  }

  @Override
  public void loadFromObject(Object object) {
    setLore(CollectionUtils.createStringListFromObject(object, false));
  }

  /**
   * Set the lore
   *
   * @param lore the lore
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public LoreModifier setLore(String... lore) {
    return setLore(Arrays.asList(lore));
  }

  /**
   * Add a lore
   *
   * @param lore the lore
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public LoreModifier addLore(String lore) {
    this.lore.addAll(Arrays.asList(lore.split("\\n")));
    return this;
  }

  /**
   * Set the lore
   *
   * @param lore the lore
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public LoreModifier setLore(Collection<String> lore) {
    clearLore();
    this.lore.addAll(CollectionUtils.splitAll("\\n", lore));
    return this;
  }

  /**
   * Clear the lore
   *
   * @return {@code this} for builder chain
   */
  @Contract(" -> this")
  public LoreModifier clearLore() {
    this.lore.clear();
    return this;
  }
}
