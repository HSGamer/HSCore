package me.hsgamer.hscore.config.hjson;

import me.hsgamer.hscore.config.BaseConfig;
import me.hsgamer.simplehjson.HJsonConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A HJSON config file
 */
public class HJsonConfig extends BaseConfig<HJsonConfiguration> {

  /**
   * Create a HJSON config
   *
   * @param file the config file
   */
  public HJsonConfig(@NotNull final File file) {
    super(file, new HJsonProvider());
  }

  /**
   * Create a HJSON config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public HJsonConfig(@NotNull final File dataFolder, @NotNull final String filename) {
    this(new File(dataFolder, filename));
  }
}
