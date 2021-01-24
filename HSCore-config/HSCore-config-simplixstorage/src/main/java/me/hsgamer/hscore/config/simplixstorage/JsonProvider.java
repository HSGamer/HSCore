package me.hsgamer.hscore.config.simplixstorage;

import de.leonhard.storage.Json;
import me.hsgamer.hscore.config.ConfigProvider;

import java.io.File;

/**
 * The Json provider
 */
public class JsonProvider implements ConfigProvider<LightningConfig<Json>> {
  @Override
  public LightningConfig<Json> loadConfiguration(File file) {
    return new LightningConfig<>(new Json(file));
  }
}
