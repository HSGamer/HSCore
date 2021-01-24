package me.hsgamer.hscore.config.simplixstorage;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import me.hsgamer.hscore.config.Config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The {@link Config} implementation for SimplixStorage
 */
public class LightningConfig implements Config {
  private final FlatFile flatFile;

  /**
   * Create a new config
   *
   * @param flatFile the flat file
   */
  public LightningConfig(FlatFile flatFile) {
    this.flatFile = flatFile;
  }

  @Override
  public Object getOriginal() {
    return flatFile;
  }

  @Override
  public Object get(String path, Object def) {
    return flatFile.get(path, def);
  }

  @Override
  public void set(String path, Object value) {
    flatFile.set(path, value);
  }

  @Override
  public boolean contains(String path) {
    return flatFile.contains(path);
  }

  @Override
  public String getName() {
    return flatFile.getFilePath();
  }

  @Override
  public void addDefault(String path, Object value) {
    flatFile.setDefault(path, value);
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    Set<String> paths;
    if (path == null || path.isEmpty()) {
      paths = deep ? flatFile.keySet() : flatFile.singleLayerKeySet();
    } else {
      paths = deep ? flatFile.keySet(path) : flatFile.singleLayerKeySet(path);
    }
    Map<String, Object> values = new LinkedHashMap<>();
    paths.forEach(p -> values.put(p, flatFile.get(p)));
    return values;
  }

  @Override
  public void setup() {
    flatFile.setReloadSettings(ReloadSettings.AUTOMATICALLY);
  }

  @Override
  public void save() {
    flatFile.write();
  }

  @Override
  public void reload() {
    flatFile.forceReload();
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
