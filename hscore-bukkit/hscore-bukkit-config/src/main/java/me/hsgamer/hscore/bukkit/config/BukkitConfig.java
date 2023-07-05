package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.CommentType;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.logger.common.LogLevel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The bukkit configuration
 */
public class BukkitConfig implements Config {
  private static boolean isCommentSupported;

  static {
    try {
      //noinspection ConstantValue
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

  /**
   * Create a new config, the file name will be "config.yml"
   *
   * @param plugin the plugin
   */
  public BukkitConfig(Plugin plugin) {
    this(plugin, "config.yml");
  }

  private String toPath(PathString pathString) {
    return PathString.toPath(String.valueOf(configuration.options().pathSeparator()), pathString);
  }

  private Map<PathString, Object> toPathStringMap(Map<String, Object> map) {
    return PathString.toPathStringMap(String.valueOf(configuration.options().pathSeparator()), map);
  }

  @Override
  public YamlConfiguration getOriginal() {
    return this.configuration;
  }

  @Override
  public Object get(PathString path, Object def) {
    return this.configuration.get(toPath(path), def);
  }

  @Override
  public void set(PathString path, Object value) {
    this.configuration.set(toPath(path), value);
  }

  @Override
  public boolean contains(PathString path) {
    return this.configuration.isSet(toPath(path));
  }

  @Override
  public String getName() {
    return this.file.getName();
  }

  @Override
  public void addDefault(PathString path, Object value) {
    this.configuration.addDefault(toPath(path), value);
  }

  @Override
  public Map<PathString, Object> getValues(PathString path, boolean deep) {
    if (path.isRoot()) {
      return toPathStringMap(this.configuration.getValues(deep));
    } else {
      return Optional.ofNullable(this.configuration.getConfigurationSection(toPath(path)))
        .map(configurationSection -> configurationSection.getValues(deep))
        .map(this::toPathStringMap)
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
        LOGGER.log(LogLevel.WARN, "Something wrong when creating " + this.file.getName(), e);
      }
    }
    this.configuration.options().copyDefaults(true);
    try {
      this.configuration.load(this.file);
    } catch (IOException | InvalidConfigurationException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when loading " + this.file.getName(), e);
    }
  }

  @Override
  public void save() {
    try {
      this.configuration.save(this.file);
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when saving " + this.file.getName(), e);
    }
  }

  @Override
  public void reload() {
    List<String> keys = new ArrayList<>(this.configuration.getKeys(false));
    keys.forEach(key -> this.configuration.set(key, null));
    setup();
  }

  @Override
  public List<String> getComment(PathString path, CommentType type) {
    if (path.isRoot()) {
      String header = this.configuration.options().header();
      return header.isEmpty() ? Collections.emptyList() : Arrays.asList(header.split("\\r?\\n"));
    }

    if (!isCommentSupported) return Collections.emptyList();
    List<String> comments;
    switch (type) {
      case BLOCK:
        comments = this.configuration.getComments(toPath(path));
        break;
      case SIDE:
        comments = this.configuration.getInlineComments(toPath(path));
        break;
      default:
        comments = Collections.emptyList();
        break;
    }
    return comments;
  }

  @Override
  public void setComment(PathString path, List<String> value, CommentType type) {
    if (path.isRoot()) {
      this.configuration.options()
        .copyHeader(true)
        .header(value == null || value.isEmpty() ? null : String.join(System.lineSeparator(), value));
      return;
    }

    if (!isCommentSupported) return;
    switch (type) {
      case BLOCK:
        this.configuration.setComments(toPath(path), value);
        break;
      case SIDE:
        this.configuration.setInlineComments(toPath(path), value);
        break;
      default:
        break;
    }
  }
}
