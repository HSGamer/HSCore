package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.skull.SkullHandler;
import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The skull modifier
 */
@SuppressWarnings("deprecation")
public class SkullModifier implements ItemMetaModifier, ItemMetaComparator {
  /**
   * <a href="https://github.com/CryptoMorin/XSeries/blob/f7e0e83de5c657229ece8ae5ca386cee581fb8e1/src/main/java/com/cryptomorin/xseries/SkullUtils.java#L95C5-L95C92">...</a>
   */
  private static final Pattern MOJANG_SHA256_APPROX = Pattern.compile("[a-z0-9]{60,70}");
  private static final SkullMeta delegateSkullMeta;
  private static final SkullHandler skullHandler;

  static {
    ItemStack itemStack;
    if (VersionUtils.isAtLeast(13)) {
      itemStack = new ItemStack(Material.valueOf("PLAYER_HEAD"));
    } else {
      itemStack = new ItemStack(Material.valueOf("SKULL_ITEM"));
      itemStack.setDurability((short) 3);
    }
    delegateSkullMeta = (SkullMeta) Objects.requireNonNull(itemStack.getItemMeta());
    skullHandler = SkullHandler.getInstance();
  }

  private String skullString = "";

  private static void setSkull(SkullMeta meta, String skull) {
    if (BukkitUtils.isUsername(skull)) {
      skullHandler.setSkullByName(meta, skull);
    } else if (Validate.isValidUUID(skull)) {
      skullHandler.setSkullByUUID(meta, UUID.fromString(skull));
    } else if (Validate.isValidURL(skull)) {
      skullHandler.setSkullByURL(meta, skull);
    } else if (MOJANG_SHA256_APPROX.matcher(skull).matches()) {
      skullHandler.setSkullByURL(meta, "https://textures.minecraft.net/texture/" + skull);
    }
  }

  private static SkullMeta getSkullMeta(String skull) {
    SkullMeta meta = delegateSkullMeta.clone();
    setSkull(meta, skull);
    return meta;
  }

  private String getFinalSkullString(UUID uuid, Collection<StringReplacer> replacers) {
    return StringReplacer.replace(skullString, uuid, replacers);
  }

  @Override
  public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    if (!(meta instanceof SkullMeta)) {
      return meta;
    }
    setSkull((SkullMeta) meta, getFinalSkullString(uuid, stringReplacers));
    return meta;
  }

  @Override
  public boolean loadFromItemMeta(ItemMeta meta) {
    if (meta instanceof SkullMeta) {
      skullString = skullHandler.getSkullValue((SkullMeta) meta);
      return true;
    }
    return false;
  }

  @Override
  public boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    if (!(meta instanceof SkullMeta)) {
      return false;
    }
    return skullHandler.compareSkull(
      getSkullMeta(getFinalSkullString(uuid, stringReplacers)),
      (SkullMeta) meta
    );
  }

  @Override
  public Object toObject() {
    return skullString;
  }

  @Override
  public void loadFromObject(Object object) {
    this.skullString = String.valueOf(object);
  }

  /**
   * Set the skull
   *
   * @param skull the skull
   *
   * @return {@code this} for builder chain
   */
  public SkullModifier setSkull(String skull) {
    this.skullString = skull;
    return this;
  }
}
