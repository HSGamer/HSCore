package me.hsgamer.hscore.config.hjson;

import me.hsgamer.hscore.config.ConfigProvider;
import me.hsgamer.simplehjson.HJsonConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * a HJSON implementation for {@link ConfigProvider}.
 */
public final class HJsonProvider implements ConfigProvider<HJsonConfiguration> {

  @NotNull
  @Override
  public HJsonConfiguration loadConfiguration(@NotNull final File file) {
    return HJsonConfiguration.loadConfiguration(file);
  }
}
