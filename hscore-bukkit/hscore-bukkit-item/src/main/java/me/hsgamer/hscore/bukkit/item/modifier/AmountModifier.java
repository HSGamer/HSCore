package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * The amount modifier
 */
public class AmountModifier implements ItemModifier {
  private String amount = "1";

  @Override
  public String getName() {
    return "amount";
  }

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    Validate
      .getNumber(StringReplacer.replace(amount, uuid, stringReplacerMap.values()))
      .ifPresent(bigDecimal -> original.setAmount(bigDecimal.intValue()));
    return original;
  }

  @Override
  public Object toObject() {
    return amount;
  }

  @Override
  public void loadFromObject(Object object) {
    this.amount = String.valueOf(object);
  }

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    this.amount = String.valueOf(itemStack.getAmount());
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return Validate.getNumber(StringReplacer.replace(amount, uuid, stringReplacerMap.values()))
      .map(bigDecimal -> bigDecimal.intValue() >= itemStack.getAmount())
      .orElse(false);
  }

  /**
   * Set the amount
   *
   * @param amount the amount
   *
   * @return {@code this} for builder chain
   */
  public AmountModifier setAmount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Set the amount
   *
   * @param amount the amount
   *
   * @return {@code this} for builder chain
   */
  public AmountModifier setAmount(int amount) {
    this.amount = String.valueOf(amount);
    return this;
  }
}
