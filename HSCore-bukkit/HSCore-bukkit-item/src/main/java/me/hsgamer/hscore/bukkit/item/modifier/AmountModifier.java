package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The amount modifier
 */
public class AmountModifier implements ItemModifier {
  private String amount = "1";

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, StringReplacer replacer) {
    try {
      original.setAmount(Integer.parseInt(amount));
    } catch (NumberFormatException exception) {
      // IGNORED
    }
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
