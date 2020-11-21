package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.bukkit.item.modifier.*;
import me.hsgamer.hscore.map.CaseInsensitiveStringLinkedMap;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The builder for {@link ItemModifier}
 */
public class ItemModifierBuilder {
  private static final Map<String, Supplier<ItemModifier>> itemModifierMap = new CaseInsensitiveStringLinkedMap<>();

  static {
    itemModifierMap.put("name", NameModifier::new);
    itemModifierMap.put("lore", LoreModifier::new);
    itemModifierMap.put("material", MaterialModifier::new);
    itemModifierMap.put("amount", AmountModifier::new);
    itemModifierMap.put("durability", DurabilityModifier::new);
  }

  private ItemModifierBuilder() {
  }

  /**
   * Register a new modifier
   *
   * @param name             the name of the modifier
   * @param modifierSupplier the modifier supplier
   */
  public static void register(String name, Supplier<ItemModifier> modifierSupplier) {
    itemModifierMap.put(name, modifierSupplier);
  }

  /**
   * Unregister a modifier
   *
   * @param name the nae of the modifier
   */
  public static void unregister(String name) {
    itemModifierMap.remove(name);
  }

  /**
   * Deserialize to a modifier map from an object map
   *
   * @param stringObjectMap the object map
   *
   * @return the modifier map
   */
  public static Map<String, ItemModifier> deserializeFromMap(Map<String, Object> stringObjectMap) {
    Map<String, ItemModifier> modifierMap = new CaseInsensitiveStringLinkedMap<>();
    stringObjectMap.entrySet().stream()
      .filter(entry -> itemModifierMap.containsKey(entry.getKey()))
      .forEach(entry -> {
        String key = entry.getKey();
        ItemModifier modifier = itemModifierMap.get(key).get();
        modifier.loadFromObject(entry.getValue());
        modifierMap.put(key, modifier);
      });
    return modifierMap;
  }

  /**
   * Deserialize to a modifier map from an item
   *
   * @param itemStack the item
   *
   * @return the modifier map
   */
  public static Map<String, ItemModifier> deserializeFromItemStack(ItemStack itemStack) {
    Map<String, ItemModifier> modifierMap = new CaseInsensitiveStringLinkedMap<>();
    itemModifierMap.forEach((key, supplier) -> {
      ItemModifier modifier = supplier.get();
      if (modifier.canLoadFromItemStack(itemStack)) {
        modifier.loadFromItemStack(itemStack);
        modifierMap.put(key, modifier);
      }
    });
    return modifierMap;
  }

  /**
   * Convert an item to an item builder
   *
   * @param itemStack the item
   *
   * @return the item builder
   */
  public static ItemBuilder convertItemStackToItemBuilder(ItemStack itemStack) {
    ItemBuilder builder = new ItemBuilder();
    deserializeFromItemStack(itemStack).forEach(builder::addItemModifier);
    return builder;
  }

  /**
   * Serialize from an item builder
   *
   * @param itemBuilder the item builder
   *
   * @return the object map
   */
  public static Map<String, Object> serializeFromItemBuilder(ItemBuilder itemBuilder) {
    return serializeFromItemModifierMap(itemBuilder.getItemModifierMap());
  }

  /**
   * Serialize from a modifier map
   *
   * @param itemModifierMap the modifier map
   *
   * @return the object map
   */
  public static Map<String, Object> serializeFromItemModifierMap(Map<String, ItemModifier> itemModifierMap) {
    Map<String, Object> map = new HashMap<>();
    itemModifierMap.forEach((k, v) -> map.put(k, v.toObject()));
    return map;
  }
}
