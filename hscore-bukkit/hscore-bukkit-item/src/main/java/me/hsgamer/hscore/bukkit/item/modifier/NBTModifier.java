package me.hsgamer.hscore.bukkit.item.modifier;

import com.google.gson.Gson;
import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * The NBT Modifier
 */
public class NBTModifier implements ItemModifier {
  private static final Gson GSON = new Gson();
  private String nbtData = "";

  /**
   * Set the NBT data
   *
   * @param nbtData the NBT data
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public NBTModifier setNbtData(String nbtData) {
    this.nbtData = nbtData;
    return this;
  }

  @Override
  public String getName() {
    return "nbt";
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack modify(ItemStack original, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    if (Validate.isNullOrEmpty(nbtData)) {
      return original;
    }
    try {
      return Bukkit.getUnsafe().modifyItemStack(original, StringReplacer.replace(nbtData, uuid, stringReplacerMap.values()));
    } catch (Throwable throwable) {
      return original;
    }
  }

  @Override
  public Object toObject() {
    return nbtData;
  }

  @Override
  public void loadFromObject(Object object) {
    if (object instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) object;
      this.nbtData = GSON.toJson(map);
    } else {
      this.nbtData = Objects.toString(object, "");
    }
  }

  @Override
  public boolean loadFromItemStack(ItemStack itemStack) {
    return false;
  }

  @Override
  public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    return false;
  }
}
