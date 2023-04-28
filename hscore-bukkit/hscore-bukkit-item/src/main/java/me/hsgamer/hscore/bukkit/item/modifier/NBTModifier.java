package me.hsgamer.hscore.bukkit.item.modifier;

import com.google.gson.Gson;
import me.hsgamer.hscore.bukkit.item.ItemModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull ItemStack modify(@NotNull ItemStack original, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    if (Validate.isNullOrEmpty(nbtData)) {
      return original;
    }
    try {
      return Bukkit.getUnsafe().modifyItemStack(original, StringReplacer.replace(nbtData, uuid, stringReplacers));
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
}
