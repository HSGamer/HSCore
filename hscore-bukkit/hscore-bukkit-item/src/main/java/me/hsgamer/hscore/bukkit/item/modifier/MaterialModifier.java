package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

import java.util.*;

/**
 * The material modifier
 */
public class MaterialModifier implements ItemModifier {
  private List<String> materialList = Collections.emptyList();

  private static Material getMaterial(String materialString) {
    materialString = materialString.replace(" ", "_");
    Material material;
    if (VersionUtils.isAtLeast(13)) {
      material = Material.matchMaterial(materialString, false);
      if (material == null) {
        material = Material.matchMaterial(materialString, true);
      }
    } else {
      material = Material.matchMaterial(materialString);
    }
    return material;
  }

  @Override
  public String getName() {
    return "material";
  }

  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    for (String materialString : materialList) {
      Material material = getMaterial(StringReplacer.replace(materialString, uuid, stringReplacerMap.values()));
      if (material != null) {
        original.setType(material);
        break;
      }
    }
    return original;
  }

  @Override
  public Object toObject() {
    return this.materialList;
  }

  @Override
  public void loadFromObject(Object object) {
    this.materialList = CollectionUtils.createStringListFromObject(object, true);
  }

  @Override
  public void loadFromItemStack(ItemStack itemStack) {
    this.materialList = Collections.singletonList(itemStack.getType().name());
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (materialList.isEmpty()) {
      return true;
    }
    String material = itemStack.getType().name();
    return materialList.parallelStream()
      .map(s -> StringReplacer.replace(s, uuid, stringReplacerMap.values()))
      .anyMatch(s -> s.equalsIgnoreCase(material));
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
    this.materialList = Collections.singletonList(material.name());
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
  public MaterialModifier setMaterial(String... material) {
    this.materialList = Arrays.asList(material);
    return this;
  }
}
