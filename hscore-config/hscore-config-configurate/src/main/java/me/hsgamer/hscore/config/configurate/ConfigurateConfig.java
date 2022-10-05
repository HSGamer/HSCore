package me.hsgamer.hscore.config.configurate;

import me.hsgamer.hscore.config.Config;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

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

  private Object[] splitPath(String path) {
    if (path.isEmpty()) {
      return new Object[0];
    }
    return path.split("\\.");
  }

  @Override
  public Object get(String path, Object def) {
    Object[] splitPath = splitPath(path);
    ConfigurationNode node = this.rootNode.node(splitPath);
    if (node.virtual()) {
      return def;
    }
    Object value = node.rawScalar();
    return value == null ? def : value;
  }

  @Override
  public void set(String path, Object value) {
    Object[] splitPath = splitPath(path);
    ConfigurationNode node = this.rootNode.node(splitPath);
    try {
      node.set(value);
    } catch (SerializationException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when setting " + path);
    }
  }

  @Override
  public boolean contains(String path) {
    return !rootNode.node(splitPath(path)).virtual();
  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    Object[] splitPath = splitPath(path);
    ConfigurationNode node;
    if (splitPath.length == 0) {
      node = this.rootNode;
    } else {
      node = this.rootNode.node(splitPath);
    }
    if (node.virtual()) {
      return Collections.emptyMap();
    }
    Map<String, Object> map = new LinkedHashMap<>();
    for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
      String key = Objects.toString(entry.getKey());
      ConfigurationNode value = entry.getValue();
      map.put(key, value);
      if (value.isMap() && deep) {
        Map<String, Object> subMap = getValues(path + "." + key, true);
        for (Map.Entry<String, Object> subEntry : subMap.entrySet()) {
          map.put(key + "." + subEntry.getKey(), subEntry.getValue());
        }
      }
    }
    return map;
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
    this.loader = this.builder.file(file).build();
    this.reload();
  }

  @Override
  public void save() {
    try {
      this.loader.save(this.rootNode);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + this.file.getName());
    }
  }

  @Override
  public void reload() {
    try {
      this.rootNode = this.loader.load();
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when loading " + this.file.getName());
    }
  }

  @Override
  public Object normalize(Object object) {
    if (object instanceof ConfigurationNode) {
      ConfigurationNode node = (ConfigurationNode) object;
      if (node.virtual()) {
        return null;
      } else if (node.isList()) {
        return node.childrenList();
      } else if (node.isMap()) {
        Map<String, Object> map = new LinkedHashMap<>();
        node.childrenMap().forEach((key, value) -> map.put(Objects.toString(key), value));
        return map;
      } else {
        return node.rawScalar();
      }
    }
    return object;
  }

  @Override
  public boolean isNormalizable(Object object) {
    return object instanceof ConfigurationNode;
  }
}
