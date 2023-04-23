package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * The durability modifier
 */
@SuppressWarnings("deprecation")
public class DurabilityModifier implements ItemModifier {
  private String durability = "1";

  @Override
  public String getName() {
    return "durability";
  }

  @Override
  public @NotNull ItemStack modify(@NotNull ItemStack original, UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap) {
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
  public boolean loadFromItemStack(ItemStack itemStack) {
    this.durability = String.valueOf(itemStack.getDurability());
    return true;
  }

  @Override
  public boolean compareWithItemStack(@NotNull ItemStack itemStack, UUID uuid, @NotNull Map<String, StringReplacer> stringReplacerMap) {
    return Validate.getNumber(StringReplacer.replace(durability, uuid, stringReplacerMap.values()))
      .map(bigDecimal -> bigDecimal.shortValue() == itemStack.getDurability())
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
