package me.hsgamer.hscore.config.simplixstorage;

import de.leonhard.storage.Json;
import de.leonhard.storage.Toml;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.DataStorage;
import de.leonhard.storage.internal.FlatFile;
import me.hsgamer.hscore.config.Config;

import java.io.File;
import java.util.*;

import static me.hsgamer.hscore.config.PathString.joinDefault;
import static me.hsgamer.hscore.config.PathString.splitDefault;

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

  @Override
  public F getOriginal() {
    return this.flatFile;
  }

  @Override
  public Object get(Object def, String... path) {
    return this.flatFile.get(joinDefault(path), def);
  }

  @Override
  public void set(Object value, String... path) {
    this.flatFile.set(joinDefault(path), value);
  }

  @Override
  public boolean contains(String... path) {
    return this.flatFile.contains(joinDefault(path));
  }

  @Override
  public String getName() {
    return this.flatFile.getFilePath();
  }

  @Override
  public void setIfAbsent(Object value, String... path) {
    this.flatFile.setDefault(joinDefault(path), value);
  }

  @Override
  public Set<String[]> getKeys(boolean deep, String... path) {
    Set<String> ketStrings;
    if (path.length == 0) {
      ketStrings = deep ? this.flatFile.keySet() : this.flatFile.singleLayerKeySet();
    } else {
      ketStrings = deep ? this.flatFile.keySet(joinDefault(path)) : this.flatFile.singleLayerKeySet(joinDefault(path));
    }
    if (ketStrings == null) {
      return Collections.emptySet();
    }

    Set<String[]> pathStrings = new LinkedHashSet<>();
    for (String keyString : ketStrings) {
      pathStrings.add(splitDefault(keyString));
    }
    return pathStrings;
  }

  @Override
  public Map<String[], Object> getValues(boolean deep, String... path) {
    DataStorage dataStorage;
    if (path.length == 0) {
      dataStorage = this.flatFile;
    } else if (this.flatFile.contains(joinDefault(path))) {
      dataStorage = this.flatFile.getSection(joinDefault(path));
    } else {
      return Collections.emptyMap();
    }
    Map<String[], Object> values = new LinkedHashMap<>();
    getKeys(deep, path).forEach(p -> values.put(p, dataStorage.get(joinDefault(p))));
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
