package me.hsgamer.hscore.bukkit.config.file;

import org.simpleyaml.configuration.file.FileConfigurationOptions;
import org.simpleyaml.utils.Validate;

/**
 * Wrapper of {@link org.bukkit.configuration.file.YamlConfigurationOptions} for Simple-YAML
 */
public class BukkitYamlConfigurationOptions extends FileConfigurationOptions {

  private int indent = 2;

  protected BukkitYamlConfigurationOptions(BukkitYamlConfiguration configuration) {
    super(configuration);
  }

  @Override
  public BukkitYamlConfiguration configuration() {
    return (BukkitYamlConfiguration) super.configuration();
  }

  @Override
  public BukkitYamlConfigurationOptions copyDefaults(final boolean value) {
    super.copyDefaults(value);
    return this;
  }

  @Override
  public BukkitYamlConfigurationOptions pathSeparator(final char value) {
    super.pathSeparator(value);
    return this;
  }

  @Override
  public BukkitYamlConfigurationOptions header(final String value) {
    super.header(value);
    return this;
  }

  @Override
  public BukkitYamlConfigurationOptions copyHeader(final boolean value) {
    super.copyHeader(value);
    return this;
  }

  public int indent() {
    return this.indent;
  }

  public BukkitYamlConfigurationOptions indent(final int value) {
    Validate.isTrue(value >= 2, "Indent must be at least 2 characters");
    Validate.isTrue(value <= 9, "Indent cannot be greater than 9 characters");

    this.indent = value;
    return this;
  }
}
