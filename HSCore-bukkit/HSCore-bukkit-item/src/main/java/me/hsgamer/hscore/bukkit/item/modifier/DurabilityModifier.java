package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * The durability modifier
 */
public class DurabilityModifier implements ItemModifier {
  private String durability = "1";

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    Validate
      .getNumber(StringReplacer.replace(durability, uuid, stringReplacerMap.values()))
      .ifPresent(bigDecimal -> original.setDurability(bigDecimal.shortValue()));
    return original;
  }

  @Override
  public Object toObject() {
    return durability;
  }

  @Override
  public void loadFromObject(Object object) {
    this.durability = String.valueOf(object);
  }

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    this.durability = String.valueOf(itemStack.getDurability());
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return Validate.getNumber(StringReplacer.replace(durability, uuid, stringReplacerMap.values()))
      .map(bigDecimal -> bigDecimal.shortValue() == itemStack.getDurability())
      .orElse(false);
  }

  /**
   * Set the durability
   *
   * @param durability the durability
   */
  public DurabilityModifier setAmount(String durability) {
    this.durability = durability;
    return this;
  }

  /**
   * Set the durability
   *
   * @param durability the durability
   */
  public DurabilityModifier setAmount(short durability) {
    this.durability = String.valueOf(durability);
    return this;
  }
}
