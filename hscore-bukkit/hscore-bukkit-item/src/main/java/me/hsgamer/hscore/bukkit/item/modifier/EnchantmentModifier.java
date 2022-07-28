package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The enchantment modifier
 */
public class EnchantmentModifier extends ItemMetaModifier {
  private static final Map<String, Enchantment> ENCHANTMENT_MAP = new HashMap<>();

  static {
    for (Enchantment enchantment : Enchantment.values()) {
      ENCHANTMENT_MAP.put(normalizeEnchantmentName(enchantment.getName()), enchantment);
    }
  }

  private List<String> enchantmentList = Collections.emptyList();

  private static String normalizeEnchantmentName(String name) {
    return name.toUpperCase(Locale.ROOT).replace(" ", "_");
  }

  @Override
  public String getName() {
    return "enchantment";
  }

  private Map<Enchantment, Integer> getParsed(UUID uuid, Collection<StringReplacer> stringReplacers) {
    Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
    for (String string : enchantmentList) {
      String[] split = StringReplacer.replace(string, uuid, stringReplacers).split(",", 2);
      Optional<Enchantment> enchantment = Optional.of(split[0].trim()).map(EnchantmentModifier::normalizeEnchantmentName).map(ENCHANTMENT_MAP::get);
      int level = 1;
      if (split.length > 1) {
        String rawLevel = split[1].trim();
        Optional<BigDecimal> optional = Validate.getNumber(rawLevel);
        if (optional.isPresent()) {
          level = optional.get().intValue();
        } else {
          continue;
        }
      }
      if (enchantment.isPresent()) {
        enchantments.put(enchantment.get(), level);
      }
    }
    return enchantments;
  }

  @Override
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    Map<Enchantment, Integer> map = getParsed(uuid, stringReplacerMap.values());
    if (map instanceof EnchantmentStorageMeta) {
      map.forEach((enchant, level) -> ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant, level, true));
    } else {
      map.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
    }
    return meta;
  }

  @Override
  public void loadFromItemMeta(ItemMeta meta) {
    this.enchantmentList = meta.getEnchants().entrySet()
      .stream()
      .map(entry -> entry.getKey().getName() + ", " + entry.getValue())
      .collect(Collectors.toList());
  }

  @Override
  public boolean canLoadFromItemMeta(ItemMeta meta) {
    return meta.hasEnchants();
  }

  @Override
  public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    Map<Enchantment, Integer> list1 = getParsed(uuid, stringReplacerMap.values());
    Map<Enchantment, Integer> list2 = meta.getEnchants();
    if (list1.size() != list2.size()) {
      return false;
    }
    for (Map.Entry<Enchantment, Integer> entry : list1.entrySet()) {
      Enchantment enchantment = entry.getKey();
      int lvl = entry.getValue();
      if (!list2.containsKey(enchantment) || list2.get(enchantment) != lvl) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Object toObject() {
    return enchantmentList;
  }

  @Override
  public void loadFromObject(Object object) {
    this.enchantmentList = CollectionUtils.createStringListFromObject(object, true);
  }

  /**
   * Add an enchantment
   *
   * @param enchantment the enchantment
   * @param level       the level
   *
   * @return {@code this} for builder chain
   */
  @Contract("_, _ -> this")
  public EnchantmentModifier addEnchantment(String enchantment, int level) {
    enchantmentList.add(enchantment + ", " + level);
    return this;
  }

  /**
   * Add an enchantment
   *
   * @param enchantment the enchantment
   * @param level       the level
   *
   * @return {@code this} for builder chain
   */
  @Contract("_, _ -> this")
  public EnchantmentModifier addEnchantment(Enchantment enchantment, int level) {
    enchantmentList.add(normalizeEnchantmentName(enchantment.getName()) + ", " + level);
    return this;
  }

  /**
   * Add an enchantment
   *
   * @param enchantment the enchantment
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public EnchantmentModifier addEnchantment(String enchantment) {
    enchantmentList.add(enchantment);
    return this;
  }

  /**
   * Add an enchantment
   *
   * @param enchantment the enchantment
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public EnchantmentModifier addEnchantment(Enchantment enchantment) {
    enchantmentList.add(normalizeEnchantmentName(enchantment.getName()));
    return this;
  }
}