package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.skull.SkullHandler;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The skull modifier
 */
@SuppressWarnings("deprecation")
public class SkullModifier implements ItemMetaModifier, ItemMetaComparator {
  /**
   * @see <a href="https://github.com/CryptoMorin/XSeries/blob/eaaa6dedbf542ea8a38c6a4726538508d9e4f8e7/src/main/java/com/cryptomorin/xseries/SkullUtils.java#L96C74-L96C89">XSeries</a>
   */
  private static final Pattern MOJANG_SHA256_APPROX = Pattern.compile("[0-9a-z]{55,70}");
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
    Optional<URL> url = Validate.getURL(skull);
    if (url.isPresent()) {
      skullHandler.setSkullByURL(meta, url.get());
      return;
    }

    if (MOJANG_SHA256_APPROX.matcher(skull).matches()) {
      skullHandler.setSkullByURL(meta, "https://textures.minecraft.net/texture/" + skull);
      return;
    }

    Optional<UUID> uuid = Validate.getUUID(skull);
    if (uuid.isPresent()) {
      skullHandler.setSkullByUUID(meta, uuid.get());
      return;
    }

    skullHandler.setSkullByName(meta, skull);
  }

  private static SkullMeta getSkullMeta(String skull) {
    SkullMeta meta = delegateSkullMeta.clone();
    setSkull(meta, skull);
    return meta;
  }

  @Override
  public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer) {
    if (!(meta instanceof SkullMeta)) {
      return meta;
    }
    setSkull((SkullMeta) meta, stringReplacer.tryReplace(skullString, uuid));
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
  public boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull StringReplacer stringReplacer) {
    if (!(meta instanceof SkullMeta)) {
      return false;
    }
    return skullHandler.compareSkull(
      getSkullMeta(stringReplacer.tryReplace(skullString, uuid)),
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
