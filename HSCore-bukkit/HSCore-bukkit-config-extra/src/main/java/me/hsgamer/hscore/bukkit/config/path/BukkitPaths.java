package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.ConfigPath;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

/**
 * Utility to easily create {@link ConfigPath}
 */
public final class BukkitPaths {

  private BukkitPaths() {
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static AttributeModifierConfigPath attributeModifierPath(final String path, final AttributeModifier def) {
    return new AttributeModifierConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static ColorConfigPath colorPath(final String path, final Color def) {
    return new ColorConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static FireworkEffectConfigPath fireworkEffectPath(final String path, final FireworkEffect def) {
    return new FireworkEffectConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static ItemStackConfigPath itemStackPath(final String path, final ItemStack def) {
    return new ItemStackConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static LocationConfigPath locationPath(final String path, final Location def) {
    return new LocationConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static PatternConfigPath patternPath(final String path, final Pattern def) {
    return new PatternConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static PotionEffectConfigPath potionEffectPath(final String path, final PotionEffect def) {
    return new PotionEffectConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the config path
   */
  public static VectorConfigPath vectorPath(final String path, final Vector def) {
    return new VectorConfigPath(path, def);
  }

}
