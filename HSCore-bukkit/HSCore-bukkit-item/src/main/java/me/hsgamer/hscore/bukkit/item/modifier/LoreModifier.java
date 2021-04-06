package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The lore modifier
 */
public class LoreModifier extends ItemMetaModifier {
  private final List<String> lore = new ArrayList<>();

  @Override
  public String getName() {
    return "lore";
  }

  private List<String> getReplacedLore(UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    List<String> replacedLore = new ArrayList<>(lore);
    replacedLore.replaceAll(s -> StringReplacer.replace(s, uuid, stringReplacerMap.values()));
    return replacedLore;
  }

  @Override
  public void modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    meta.setLore(getReplacedLore(uuid, stringReplacerMap));
  }

  @Override
  public void loadFromItemMeta(ItemMeta meta) {
    setLore(meta.getLore());
  }

  @Override
  public boolean canLoadFromItemMeta(ItemMeta meta) {
    return meta.hasLore();
  }

  @Override
  public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return (!meta.hasLore() && this.lore.isEmpty()) || getReplacedLore(uuid, stringReplacerMap).equals(meta.getLore());
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
  public LoreModifier clearLore() {
    this.lore.clear();
    return this;
  }
}
