package me.hsgamer.hscore.config.annotated;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.DecorativeConfig;
import me.hsgamer.hscore.config.annotation.Comment;
import me.hsgamer.hscore.config.annotation.ConfigPath;
import me.hsgamer.hscore.config.annotation.converter.Converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * The annotated {@link Config}, where any fields can be assigned to the config with the annotation {@link ConfigPath}
 */
public class AnnotatedConfig extends DecorativeConfig {
  private final List<Field> pathFields = new ArrayList<>();

  /**
   * Create an annotated config
   *
   * @param config the original config
   */
  public AnnotatedConfig(Config config) {
    super(config);
  }

  @Override
  public void setup() {
    super.setup();
    for (Field field : this.getClass().getDeclaredFields()) {
      if (checkPathField(field)) {
        this.pathFields.add(field);
      }
    }
    this.pathFields.forEach(this::setupField);
    setupClassComment();
    this.save();
  }

  @Override
  public void set(String path, Object value) {
    pathFields.forEach(field -> {
      ConfigPath configPath = field.getAnnotation(ConfigPath.class);
      if (configPath.value().equals(path)) {
        checkAndSetField(field, configPath, value);
      }
    });
  }

  @Override
  public void reload() {
    super.reload();
    for (Field field : this.getClass().getDeclaredFields()) {
      setupField(field);
    }
    setupClassComment();
    this.save();
  }

  private void setupClassComment() {
    if (this.getClass().isAnnotationPresent(Comment.class) && this.getComment("") == null) {
      this.setComment("", this.getClass().getAnnotation(Comment.class).value());
    }
  }

  private boolean checkPathField(Field field) {
    ConfigPath configPath = field.getAnnotation(ConfigPath.class);
    if (configPath == null) {
      return false;
    }
    if (Modifier.isFinal(field.getModifiers())) {
      LOGGER.warning(() -> field.getName() + " is a final field. Ignored");
      return false;
    }
    try {
      Converter.createConverterSafe(configPath.converter());
    } catch (Exception e) {
      LOGGER.warning(() -> "Cannot create a converter for " + field.getName() + ". Ignored");
      return false;
    }
    return true;
  }

  private void setupField(Field field) {
    ConfigPath configPath = field.getAnnotation(ConfigPath.class);
    String path = configPath.value();
    Converter converter = Converter.createConverterSafe(configPath.converter());
    Object defaultValue = this.getValue(field);

    if (!contains(path)) {
      super.set(path, converter.convertToRaw(defaultValue));
      if (field.isAnnotationPresent(Comment.class)) {
        super.setComment(path, field.getAnnotation(Comment.class).value());
      }
    } else {
      this.setValue(field, converter.convert(this.getNormalized(path)));
    }
  }

  private void checkAndSetField(Field field, ConfigPath configPath, Object value) {
    String path = configPath.value();
    Converter converter = Converter.createConverterSafe(configPath.converter());
    super.set(path, converter.convertToRaw(value));
    this.setValue(field, value);
  }

  private void setValue(Field field, Object value) {
    boolean accessible = field.isAccessible();
    field.setAccessible(true);
    try {
      field.set(this, value);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Cannot set the value for " + field.getName(), e);
    } finally {
      field.setAccessible(accessible);
    }
  }

  private Object getValue(Field field) {
    boolean accessible = field.isAccessible();
    field.setAccessible(true);
    Object value;
    try {
      value = field.get(this);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Cannot get the value for " + field.getName(), e);
    } finally {
      field.setAccessible(accessible);
    }
    return value;
  }
}
