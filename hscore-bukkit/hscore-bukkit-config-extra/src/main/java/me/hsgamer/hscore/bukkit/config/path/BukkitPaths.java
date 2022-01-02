package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.bukkit.config.object.PlayableSound;
import me.hsgamer.hscore.bukkit.config.object.Position;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

/**
 * Utility to easily create Bukkit's {@link me.hsgamer.hscore.config.AdvancedConfigPath}
 */
public final class BukkitPaths {

  private BukkitPaths() {
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static AttributeModifierConfigPath attributeModifierPath(String path, AttributeModifier def) {
    return new AttributeModifierConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static ColorConfigPath colorPath(String path, Color def) {
    return new ColorConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static FireworkEffectConfigPath fireworkEffectPath(String path, FireworkEffect def) {
    return new FireworkEffectConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static ItemStackConfigPath itemStackPath(String path, ItemStack def) {
    return new ItemStackConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static LocationConfigPath locationPath(String path, Location def) {
    return new LocationConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static PatternConfigPath patternPath(String path, Pattern def) {
    return new PatternConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static PotionEffectConfigPath potionEffectPath(String path, PotionEffect def) {
    return new PotionEffectConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static VectorConfigPath vectorPath(String path, Vector def) {
    return new VectorConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static SoundConfigPath soundPath(String path, PlayableSound def) {
    return new SoundConfigPath(path, def);
  }

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   *
   * @return the config path
   */
  public static PositionConfigPath positionPath(String path, Position def) {
    return new PositionConfigPath(path, def);
  }
}
