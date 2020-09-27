package me.hsgamer.hscore.bukkit.config.file;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.simpleyaml.configuration.Configuration;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import org.simpleyaml.utils.Validate;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;

/**
 * A wrapper for {@link org.bukkit.configuration.file.YamlConfiguration} for Simple-YAML
 */
public class BukkitYamlConfiguration extends FileConfiguration {
  protected static final String COMMENT_PREFIX = "# ";
  protected static final String BLANK_CONFIG = "{}\n";
  private static Method buildHeaderMethod = null;

  static {
    try {
      buildHeaderMethod = FileConfiguration.class.getDeclaredMethod("buildHeader");
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  private final DumperOptions yamlOptions = new DumperOptions();
  private final Representer yamlRepresenter = new YamlRepresenter();
  private final Yaml yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);

  public static BukkitYamlConfiguration loadConfiguration(File file) {
    Validate.notNull(file, "File cannot be null");

    BukkitYamlConfiguration config = new BukkitYamlConfiguration();

    try {
      config.load(file);
    } catch (FileNotFoundException ex) {
      // IGNORED
    } catch (IOException | InvalidConfigurationException ex) {
      Bukkit.getLogger().log(Level.SEVERE, ex, () -> "Cannot load " + file);
    }

    return config;
  }

  public static BukkitYamlConfiguration loadConfiguration(Reader reader) {
    Validate.notNull(reader, "Stream cannot be null");

    BukkitYamlConfiguration config = new BukkitYamlConfiguration();

    try {
      config.load(reader);
    } catch (IOException | InvalidConfigurationException ex) {
      Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
    }

    return config;
  }

  @Override
  public String saveToString() {
    this.yamlOptions.setIndent(this.options().indent());
    this.yamlOptions.setAllowUnicode(this.options().isUnicode());
    this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    final String header = this.buildHeader();
    String dump = this.yaml.dump(this.getValues(false));

    if (dump.equals(BukkitYamlConfiguration.BLANK_CONFIG)) {
      dump = "";
    }

    return header + dump;
  }

  @Override
  public void loadFromString(final String contents) throws InvalidConfigurationException {
    Validate.notNull(contents, "Contents cannot be null");

    final Map<?, ?> input;
    try {
      input = this.yaml.load(contents);
    } catch (final YAMLException e) {
      throw new InvalidConfigurationException(e);
    } catch (final ClassCastException e) {
      throw new InvalidConfigurationException("Top level is not a Map.");
    }

    final String header = this.parseHeader(contents);
    if (header.length() > 0) {
      this.options().header(header);
    }

    if (input != null) {
      this.convertMapsToSections(input, this);
    }
  }

  @Override
  protected String buildHeader() {
    final String header = this.options().header();

    if (this.options().copyHeader()) {
      final Configuration def = this.getDefaults();

      if (def instanceof FileConfiguration) {
        final FileConfiguration fileDefaults = (FileConfiguration) def;
        String defaultsHeader;
        try {
          defaultsHeader = buildHeaderMethod.invoke(fileDefaults).toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
          defaultsHeader = null;
        }

        if (defaultsHeader != null && defaultsHeader.length() > 0) {
          return defaultsHeader;
        }
      }
    }

    if (header == null) {
      return "";
    }

    final StringBuilder builder = new StringBuilder();
    final String[] lines = header.split("\r?\n", -1);
    boolean startedHeader = false;

    for (int i = lines.length - 1; i >= 0; i--) {
      builder.insert(0, "\n");

      if (startedHeader || lines[i].length() != 0) {
        builder.insert(0, lines[i]);
        builder.insert(0, COMMENT_PREFIX);
        startedHeader = true;
      }
    }

    return builder.toString();
  }

  @Override
  public BukkitYamlConfigurationOptions options() {
    if (this.options == null) {
      this.options = new BukkitYamlConfigurationOptions(this);
    }
    return (BukkitYamlConfigurationOptions) this.options;
  }

  protected void convertMapsToSections(final Map<?, ?> input, final ConfigurationSection section) {
    for (final Map.Entry<?, ?> entry : input.entrySet()) {
      final String key = entry.getKey().toString();
      final Object value = entry.getValue();

      if (value instanceof Map) {
        this.convertMapsToSections((Map<?, ?>) value, section.createSection(key));
      } else {
        section.set(key, value);
      }
    }
  }

  protected String parseHeader(final String input) {
    final String[] lines = input.split("\r?\n", -1);
    final StringBuilder result = new StringBuilder();
    boolean readingHeader = true;
    boolean foundHeader = false;

    for (int i = 0; i < lines.length && readingHeader; i++) {
      final String line = lines[i];

      if (line.startsWith(COMMENT_PREFIX)) {
        if (i > 0) {
          result.append("\n");
        }

        if (line.length() > COMMENT_PREFIX.length()) {
          result.append(line.substring(COMMENT_PREFIX.length()));
        }

        foundHeader = true;
      } else if (foundHeader && line.length() == 0) {
        result.append("\n");
      } else if (foundHeader) {
        readingHeader = false;
      }
    }

    return result.toString();
  }
}
