package me.hsgamer.hscore.config.configurate;

import me.hsgamer.hscore.config.CommentType;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.logger.common.LogLevel;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The {@link Config} implementation for Configurate
 */
public class ConfigurateConfig implements Config {
  private final File file;
  private final AbstractConfigurationLoader.Builder<?, ?> builder;
  private ConfigurationLoader<? extends ConfigurationNode> loader;
  private ConfigurationNode rootNode;

  /**
   * Create a new config
   *
   * @param file    the file
   * @param builder the config builder
   */
  public ConfigurateConfig(File file, AbstractConfigurationLoader.Builder<?, ?> builder) {
    this.file = file;
    this.builder = builder;
  }

  @Override
  public Object getOriginal() {
    return this.rootNode;
  }

  @Override
  public Object get(PathString path, Object def) {
    ConfigurationNode node = this.rootNode.node(path.getPathAsObject());
    Object value = node.raw();
    return value == null ? def : value;
  }

  @Override
  public void set(PathString path, Object value) {
    ConfigurationNode node = this.rootNode.node(path.getPathAsObject());
    try {
      node.set(value);
    } catch (SerializationException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when setting " + path, e);
    }
  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public Map<PathString, Object> getValues(PathString path, boolean deep) {
    ConfigurationNode node;
    if (path.isRoot()) {
      node = this.rootNode;
    } else {
      node = this.rootNode.node(path.getPathAsObject());
    }
    if (!node.isMap()) {
      return Collections.emptyMap();
    }
    Map<PathString, Object> map = new LinkedHashMap<>();
    for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
      PathString key = new PathString(Objects.toString(entry.getKey()));
      ConfigurationNode value = entry.getValue();
      map.put(key, value.raw());
      if (value.isMap() && deep) {
        PathString newPath = path.append(key);
        Map<PathString, Object> subMap = getValues(newPath, true);
        for (Map.Entry<PathString, Object> subEntry : subMap.entrySet()) {
          map.put(key.append(subEntry.getKey()), subEntry.getValue());
        }
      }
    }
    return map;
  }

  @Override
  public void clear() {
    this.rootNode = this.loader.createNode();
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
    this.loader = this.builder.file(file).build();
    this.reload();
  }

  @Override
  public void save() {
    try {
      this.loader.save(this.rootNode);
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when saving " + this.file.getName(), e);
    }
  }

  @Override
  public void reload() {
    if (this.file.length() == 0) {
      this.rootNode = this.loader.createNode();
      return;
    }
    try {
      this.rootNode = this.loader.load();
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when loading " + this.file.getName(), e);
    }
  }

  @Override
  public Object normalize(Object object) {
    if (object instanceof ConfigurationNode) {
      ConfigurationNode node = (ConfigurationNode) object;
      if (node.isList()) {
        return node.childrenList();
      } else if (node.isMap()) {
        Map<String, Object> map = new LinkedHashMap<>();
        node.childrenMap().forEach((key, value) -> map.put(Objects.toString(key), value));
        return map;
      } else {
        return node.raw();
      }
    }
    return object;
  }

  @Override
  public boolean isNormalizable(Object object) {
    return object instanceof ConfigurationNode;
  }

  @Override
  public List<String> getComment(PathString path, CommentType type) {
    ConfigurationNode node = this.rootNode.node(path.getPathAsObject());
    if (!(node instanceof CommentedConfigurationNode)) return null;
    CommentedConfigurationNode commentedNode = (CommentedConfigurationNode) node;
    if (type != CommentType.BLOCK) return Collections.emptyList();
    String comment = commentedNode.comment();
    if (comment == null) return Collections.emptyList();
    return Arrays.asList(comment.split("\\r?\\n"));
  }

  @Override
  public void setComment(PathString path, List<String> value, CommentType type) {
    ConfigurationNode node = this.rootNode.node(path.getPathAsObject());
    if (!(node instanceof CommentedConfigurationNode)) return;
    CommentedConfigurationNode commentedNode = (CommentedConfigurationNode) node;
    if (type != CommentType.BLOCK) return;
    commentedNode.comment(String.join("\n", value));
  }
}
