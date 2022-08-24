package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.CommentType;
import me.hsgamer.hscore.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * The bukkit configuration
 */
public class BukkitConfig implements Config {
  private static boolean isCommentSupported;

  static {
    try {
      isCommentSupported = ConfigurationSection.class.getDeclaredMethod("getComments", String.class) != null;
    } catch (NoSuchMethodException e) {
      isCommentSupported = false;
    }
  }

  private final File file;
  private final YamlConfiguration configuration = new YamlConfiguration();

  /**
   * Create a new config
   *
   * @param file the file
   */
  public BukkitConfig(File file) {
    this.file = file;
  }

  /**
   * Create a new config
   *
   * @param plugin   the plugin
   * @param filename the file name
   */
  public BukkitConfig(Plugin plugin, String filename) {
    this(new File(plugin.getDataFolder(), filename));
    try {
      plugin.saveResource(filename, false);
    } catch (IllegalArgumentException e) {
      // IGNORED
    }
  }

  @Override
  public YamlConfiguration getOriginal() {
    return this.configuration;
  }

  @Override
  public Object get(String path, Object def) {
    return this.configuration.get(path, def);
  }

  @Override
  public void set(String path, Object value) {
    this.configuration.set(path, value);
  }

  @Override
  public boolean contains(String path) {
    return this.configuration.isSet(path);
  }

  @Override
  public String getName() {
    return this.file.getName();
  }

  @Override
  public void addDefault(String path, Object value) {
    this.configuration.addDefault(path, value);
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    if (path == null || path.isEmpty()) {
      return this.configuration.getValues(deep);
    } else {
      return Optional.ofNullable(this.configuration.getConfigurationSection(path))
        .map(configurationSection -> configurationSection.getValues(deep))
        .orElse(Collections.emptyMap());
    }
  }

  @Override
  public Object normalize(Object object) {
    if (object instanceof ConfigurationSection) {
      return ((ConfigurationSection) object).getValues(false);
    }
    return object;
  }

  @Override
  public boolean isNormalizable(Object object) {
    return object instanceof ConfigurationSection;
  }

  @Override
  public void setup() {
    if (!this.file.exists()) {
      File parentFile = this.file.getAbsoluteFile().getParentFile();
      if (parentFile != null && !parentFile.exists()) {
        parentFile.mkdirs();
      }
      try {
        this.file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + this.file.getName());
      }
    }
    this.configuration.options().copyDefaults(true);
    try {
      this.configuration.load(this.file);
    } catch (IOException | InvalidConfigurationException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when loading " + this.file.getName());
    }
  }

  @Override
  public void save() {
    try {
      this.configuration.save(this.file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + this.file.getName());
    }
  }

  @Override
  public void reload() {
    List<String> keys = new ArrayList<>(this.configuration.getKeys(false));
    keys.forEach(key -> this.configuration.set(key, null));
    setup();
  }

  @Override
  public String getComment(String path, CommentType type) {
    if (!isCommentSupported) return null;
    List<String> comments;
    switch (type) {
      case BLOCK:
        comments = this.configuration.getComments(path);
        break;
      case SIDE:
        comments = this.configuration.getInlineComments(path);
        break;
      default:
        comments = Collections.emptyList();
        break;
    }
    return comments.isEmpty() ? null : String.join("\n", comments);
  }

  @Override
  public void setComment(String path, String value, CommentType type) {
    if (!isCommentSupported) return;
    List<String> comments = value == null ? null : Arrays.asList(value.split("\n"));
    switch (type) {
      case BLOCK:
        this.configuration.setComments(path, comments);
        break;
      case SIDE:
        this.configuration.setInlineComments(path, comments);
        break;
      default:
        break;
    }
  }
}
