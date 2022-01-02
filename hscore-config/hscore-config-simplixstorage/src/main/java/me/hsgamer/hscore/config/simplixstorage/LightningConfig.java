package me.hsgamer.hscore.config.simplixstorage;

import de.leonhard.storage.internal.FlatFile;
import me.hsgamer.hscore.config.Config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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

  @Override
  public F getOriginal() {
    return this.flatFile;
  }

  @Override
  public Object get(String path, Object def) {
    return this.flatFile.get(path, def);
  }

  @Override
  public void set(String path, Object value) {
    this.flatFile.set(path, value);
  }

  @Override
  public boolean contains(String path) {
    return this.flatFile.contains(path);
  }

  @Override
  public String getName() {
    return this.flatFile.getFilePath();
  }

  @Override
  public void addDefault(String path, Object value) {
    this.flatFile.setDefault(path, value);
  }

  @Override
  public Set<String> getKeys(String path, boolean deep) {
    Set<String> keys;
    if (path == null || path.isEmpty()) {
      keys = deep ? this.flatFile.keySet() : this.flatFile.singleLayerKeySet();
    } else {
      keys = deep ? this.flatFile.keySet(path) : this.flatFile.singleLayerKeySet(path);
    }
    if (keys == null) {
      keys = Collections.emptySet();
    }
    return keys;
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    Map<String, Object> values = new LinkedHashMap<>();
    getKeys(path, deep).forEach(p -> values.put(p, this.flatFile.get(p)));
    return values;
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
