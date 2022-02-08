package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The material modifier
 */
public class MaterialModifier implements ItemModifier {
  private String materialString;

  @Override
  public String getName() {
    return "material";
  }

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    Optional
      .ofNullable(Material.matchMaterial(StringReplacer.replace(materialString, uuid, stringReplacerMap.values())))
      .ifPresent(original::setType);
    return original;
  }

  @Override
  public Object toObject() {
    return this.materialString;
  }

  @Override
  public void loadFromObject(Object object) {
    this.materialString = String.valueOf(object);
  }

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    this.materialString = itemStack.getType().name();
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return itemStack.getType().name().equalsIgnoreCase(StringReplacer.replace(materialString, uuid, stringReplacerMap.values()));
  }

  /**
   * Set the material
   *
   * @param material the material
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public MaterialModifier setMaterial(Material material) {
    this.materialString = material.name();
    return this;
  }

  /**
   * Set the material
   *
   * @param material the material
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public MaterialModifier setMaterial(String material) {
    this.materialString = material;
    return this;
  }
}
