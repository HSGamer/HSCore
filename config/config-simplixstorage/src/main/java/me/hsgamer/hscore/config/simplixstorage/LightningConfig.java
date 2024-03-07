package me.hsgamer.hscore.config.simplixstorage;

import de.leonhard.storage.Json;
import de.leonhard.storage.Toml;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.DataStorage;
import de.leonhard.storage.internal.FlatFile;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;

import java.io.File;
import java.util.*;

/**
 * The {@link Config} implementation for SimplixStorage
 *
 * @param <F> the flat file type
 */
public class LightningConfig<F extends FlatFile> implements Config {
  private final F flatFile;

  /**
   * Create a new config
   *
   * @param flatFile the flat file
   */
  public LightningConfig(F flatFile) {
    this.flatFile = flatFile;
  }

  /**
   * Create a new config from a Json file
   *
   * @param file the file
   *
   * @return the config
   */
  public static LightningConfig<Json> ofJson(File file) {
    return new LightningConfig<>(new Json(file));
  }

  /**
   * Create a new config from a Toml file
   *
   * @param file the file
   *
   * @return the config
   */
  public static LightningConfig<Toml> ofToml(File file) {
    return new LightningConfig<>(new Toml(file));
  }

  /**
   * Create a new config from a Yaml file
   *
   * @param file the file
   *
   * @return the config
   */
  public static LightningConfig<Yaml> ofYaml(File file) {
    return new LightningConfig<>(new Yaml(file));
  }

  private String toPath(PathString pathString) {
    return PathString.toPath(".", pathString);
  }

  private PathString toPathString(String path) {
    return PathString.toPathString(".", path);
  }

  private Map<String, Object> toPathMap(Map<PathString, Object> map) {
    return PathString.toPathMap(".", map);
  }

  private Map<PathString, Object> toPathStringMap(Map<String, Object> map) {
    return PathString.toPathStringMap(".", map);
  }

  @Override
  public F getOriginal() {
    return this.flatFile;
  }

  @Override
  public Object get(PathString path, Object def) {
    return this.flatFile.get(toPath(path), def);
  }

  @Override
  public void set(PathString path, Object value) {
    this.flatFile.set(toPath(path), value);
  }

  @Override
  public boolean contains(PathString path) {
    return this.flatFile.contains(toPath(path));
  }

  @Override
  public String getName() {
    return this.flatFile.getFilePath();
  }

  @Override
  public void setIfAbsent(PathString path, Object value) {
    this.flatFile.setDefault(toPath(path), value);
  }

  @Override
  public Set<PathString> getKeys(PathString path, boolean deep) {
    Set<String> ketStrings;
    if (path.isRoot()) {
      ketStrings = deep ? this.flatFile.keySet() : this.flatFile.singleLayerKeySet();
    } else {
      ketStrings = deep ? this.flatFile.keySet(toPath(path)) : this.flatFile.singleLayerKeySet(toPath(path));
    }
    if (ketStrings == null) {
      return Collections.emptySet();
    }

    Set<PathString> pathStrings = new LinkedHashSet<>();
    for (String keyString : ketStrings) {
      pathStrings.add(toPathString(keyString));
    }
    return pathStrings;
  }

  @Override
  public Map<PathString, Object> getValues(PathString path, boolean deep) {
    DataStorage dataStorage;
    if (path.isRoot()) {
      dataStorage = this.flatFile;
    } else if (this.flatFile.contains(toPath(path))) {
      dataStorage = this.flatFile.getSection(toPath(path));
    } else {
      return Collections.emptyMap();
    }
    Map<PathString, Object> values = new LinkedHashMap<>();
    getKeys(path, deep).forEach(p -> values.put(p, dataStorage.get(toPath(p))));
    return values;
  }

  @Override
  public void clear() {
    this.flatFile.getFileData().clear();
  }

  @Override
  public void setup() {
    // EMPTY
  }

  @Override
  public void save() {
    this.flatFile.write();
  }

  @Override
  public void reload() {
    this.flatFile.forceReload();
  }

  @Override
  public Object normalize(Object object) {
    return object;
  }

  @Override
  public boolean isNormalizable(Object object) {
    return false;
  }
}
