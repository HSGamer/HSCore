package me.hsgamer.hscore.config.json;

import io.github.portlek.jsongration.JsonConfiguration;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * a YAML implementation for {@link ConfigProvider}.
 */
public final class JsonProvider implements ConfigProvider<JsonConfiguration> {

  @NotNull
  @Override
  public JsonConfiguration loadConfiguration(@NotNull final File file) {
    return JsonConfiguration.loadConfiguration(file);
  }
}
