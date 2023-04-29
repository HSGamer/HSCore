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
 * The durability modifier
 */
@SuppressWarnings("deprecation")
public class DurabilityModifier implements ItemComparator<ItemStack>, ItemModifier<ItemStack> {
  private String durability = "1";

  @Override
  public @NotNull ItemStack modify(@NotNull ItemStack original, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    Validate
      .getNumber(StringReplacer.replace(durability, uuid, stringReplacers))
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
  public boolean loadFromItem(ItemStack item) {
    this.durability = String.valueOf(item.getDurability());
    return true;
  }

  @Override
  public boolean compare(@NotNull ItemStack item, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    return Validate.getNumber(StringReplacer.replace(durability, uuid, stringReplacers))
      .map(bigDecimal -> bigDecimal.shortValue() == item.getDurability())
      .orElse(false);
  }

  /**
   * Set the durability
   *
   * @param durability the durability
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public DurabilityModifier setDurability(String durability) {
    this.durability = durability;
    return this;
  }

  /**
   * Set the durability
   *
   * @param durability the durability
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public DurabilityModifier setDurability(short durability) {
    this.durability = String.valueOf(durability);
    return this;
  }
}
