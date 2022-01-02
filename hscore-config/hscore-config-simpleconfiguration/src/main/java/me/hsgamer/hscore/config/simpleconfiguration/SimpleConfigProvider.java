package me.hsgamer.hscore.config.simpleconfiguration;

import me.hsgamer.hscore.config.ConfigProvider;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * The SimpleYAML config provider
 */
public class SimpleConfigProvider<T extends FileConfiguration> implements ConfigProvider<SimpleConfig<T>> {
  private final Supplier<T> supplier;
  private final BiConsumer<File, T> loader;

  /**
   * Create a new provider
   *
   * @param supplier the supplier
   * @param loader   the loader
   */
  public SimpleConfigProvider(Supplier<T> supplier, BiConsumer<File, T> loader) {
    this.supplier = supplier;
    this.loader = loader;
  }

  @Override
  public SimpleConfig<T> loadConfiguration(File file) {
    return new SimpleConfig<>(file, supplier.get(), loader);
  }
}
