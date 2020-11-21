package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * The amount modifier
 */
public class AmountModifier implements ItemModifier {
  private String amount = "1";

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, List<StringReplacer> stringReplacers) {
    Validate
      .getNumber(StringReplacer.replace(amount, uuid, stringReplacers))
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

  /**
   * Set the amount
   *
   * @param amount the amount
   */
  public AmountModifier setAmount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Set the amount
   *
   * @param amount the amount
   */
  public AmountModifier setAmount(int amount) {
    this.amount = String.valueOf(amount);
    return this;
  }
}
