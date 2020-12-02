package me.hsgamer.hscore.bukkit.item;

import me.hsgamer.hscore.bukkit.item.modifier.*;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringHashMap;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringLinkedMap;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The builder for {@link ItemModifier}
 */
public class ItemModifierBuilder {
  private static final Map<String, Supplier<ItemModifier>> itemModifierMap = new CaseInsensitiveStringLinkedMap<>();
  private static final Map<String, String> nameMap = new CaseInsensitiveStringHashMap<>();

  static {
    registerDefaultModifiers();
  }

  private ItemModifierBuilder() {
  }

  /**
   * Register default modifiers
   */
  public static void registerDefaultModifiers() {
    register(NameModifier::new, "name");
    register(LoreModifier::new, "lore");
    register(MaterialModifier::new, "material", "mat", "id");
    register(AmountModifier::new, "amount");
    register(DurabilityModifier::new, "durability", "damage");
  }

  /**
   * Register a new modifier
   *
   * @param name             the name of the modifier
   * @param modifierSupplier the modifier supplier
   * @param aliases          the aliases of the modifier
   */
  public static void register(Supplier<ItemModifier> modifierSupplier, String name, String... aliases) {
    itemModifierMap.put(name, modifierSupplier);
    nameMap.put(name, name);
    for (String alias : aliases) {
      nameMap.put(alias, name);
    }
  }

  /**
   * Unregister a modifier
   *
   * @param name the name of the modifier
   */
  public static void unregister(String name) {
    itemModifierMap.remove(name);
    nameMap.values().removeIf(s -> s.equalsIgnoreCase(name));
  }

  /**
   * Unregister all modifiers
   */
  public static void unregisterAll() {
    itemModifierMap.clear();
    nameMap.clear();
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
      .filter(entry -> nameMap.containsKey(entry.getKey()))
      .forEach(entry -> {
        String key = nameMap.get(entry.getKey());
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
