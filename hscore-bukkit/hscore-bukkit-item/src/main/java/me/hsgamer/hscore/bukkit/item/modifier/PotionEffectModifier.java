package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The potion effect modifier
 */
public class PotionEffectModifier extends ItemMetaModifier {
  private List<String> potionEffectList = Collections.emptyList();

  private static Optional<PotionEffect> pastePotionEffect(String string) {
    String[] split = string.split(",", 3);
    PotionEffectType potionEffectType = PotionEffectType.getByName(split[0].replace(" ", "_").trim());
    if (potionEffectType == null) {
      return Optional.empty();
    }
    int duration = 2400;
    int amplifier = 0;
    if (split.length > 1) {
      duration = Validate.getNumber(split[1].trim()).map(BigDecimal::intValue).orElse(duration);
    }
    if (split.length > 2) {
      amplifier = Validate.getNumber(split[2].trim()).map(BigDecimal::intValue).orElse(amplifier);
    }
    return Optional.of(new PotionEffect(potionEffectType, duration, amplifier));
  }

  private static String serializePotionEffect(PotionEffect potionEffect) {
    return potionEffect.getType().getName() + ", " + potionEffect.getDuration() + ", " + potionEffect.getAmplifier();
  }

  private List<PotionEffect> getParsed(UUID uuid, Collection<StringReplacer> stringReplacers) {
    List<String> list = new ArrayList<>(potionEffectList);
    list.replaceAll(s -> StringReplacer.replace(s, uuid, stringReplacers));
    return list.stream()
      .map(PotionEffectModifier::pastePotionEffect)
      .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
      .collect(Collectors.toList());
  }

  @Override
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (meta instanceof PotionMeta) {
      PotionMeta potionMeta = (PotionMeta) meta;
      getParsed(uuid, stringReplacerMap.values()).forEach(potionEffect -> potionMeta.addCustomEffect(potionEffect, true));
      return potionMeta;
    }
    return meta;
  }

  @Override
  public void loadFromItemMeta(ItemMeta meta) {
    this.potionEffectList = ((PotionMeta) meta).getCustomEffects()
      .stream()
      .map(PotionEffectModifier::serializePotionEffect)
      .collect(Collectors.toList());
  }

  @Override
  public boolean canLoadFromItemMeta(ItemMeta meta) {
    return meta instanceof PotionMeta;
  }

  @Override
  public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (!(meta instanceof PotionMeta)) {
      return false;
    }
    Set<PotionEffect> list1 = new HashSet<>(getParsed(uuid, stringReplacerMap.values()));
    List<PotionEffect> list2 = ((PotionMeta) meta).getCustomEffects();
    return list1.size() == list2.size() && list1.containsAll(list2);
  }

  @Override
  public String getName() {
    return "potion-effect";
  }

  @Override
  public Object toObject() {
    return potionEffectList;
  }

  @Override
  public void loadFromObject(Object object) {
    this.potionEffectList = CollectionUtils.createStringListFromObject(object, true);
  }

  /**
   * Set the potion effect
   *
   * @param potionEffect the potion effect
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public PotionEffectModifier setPotionEffect(List<String> potionEffect) {
    this.potionEffectList = potionEffect;
    return this;
  }

  /**
   * Set the potion effect
   *
   * @param potionEffect the potion effect
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public PotionEffectModifier setPotionEffect(String... potionEffect) {
    return setPotionEffect(Arrays.asList(potionEffect));
  }

  /**
   * Set the potion effect
   *
   * @param potionEffect the potion effect
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public PotionEffectModifier setPotionEffect(Collection<PotionEffect> potionEffect) {
    return setPotionEffect(
      potionEffect.stream()
        .map(PotionEffectModifier::serializePotionEffect)
        .collect(Collectors.toList())
    );
  }

  /**
   * Set the potion effect
   *
   * @param potionEffect the potion effect
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public PotionEffectModifier setPotionEffect(PotionEffect... potionEffect) {
    return setPotionEffect(Arrays.asList(potionEffect));
  }
}
