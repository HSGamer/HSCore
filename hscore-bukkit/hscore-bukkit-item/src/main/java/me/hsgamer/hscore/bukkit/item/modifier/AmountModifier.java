package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.minecraft.item.ItemComparator;
import me.hsgamer.hscore.minecraft.item.ItemModifier;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * The amount modifier
 */
public class AmountModifier implements ItemComparator<ItemStack>, ItemModifier<ItemStack> {
  private String amount = "1";

  @Override
  public @NotNull ItemStack modify(@NotNull ItemStack original, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
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

  @Override
  public boolean loadFromItem(ItemStack item) {
    this.amount = String.valueOf(item.getAmount());
    return true;
  }

  @Override
  public boolean compare(@NotNull ItemStack item, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    return Validate.getNumber(StringReplacer.replace(amount, uuid, stringReplacers))
      .map(bigDecimal -> bigDecimal.intValue() >= item.getAmount())
      .orElse(false);
  }

  /**
   * Set the amount
   *
   * @param amount the amount
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
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
  @Contract("_ -> this")
  public AmountModifier setAmount(int amount) {
    this.amount = String.valueOf(amount);
    return this;
  }
}
