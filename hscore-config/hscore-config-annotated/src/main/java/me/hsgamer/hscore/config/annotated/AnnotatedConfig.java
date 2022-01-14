package me.hsgamer.hscore.config.annotated;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.DecorativeConfig;
import me.hsgamer.hscore.config.annotated.annotation.ConfigPath;
import me.hsgamer.hscore.config.annotated.converter.Converter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

  @NotNull
  private static Converter createConverterSafe(Class<? extends Converter> converterClass) {
    try {
      return converterClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
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
    this.save();
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
      createConverterSafe(configPath.converter());
    } catch (Exception e) {
      LOGGER.warning(() -> "Cannot create a converter for " + field.getName() + ". Ignored");
      return false;
    }
    return true;
  }

  private void setupField(Field field) {
    ConfigPath configPath = field.getAnnotation(ConfigPath.class);
    String path = configPath.value();
    Converter converter = createConverterSafe(configPath.converter());

    boolean accessible = field.isAccessible();
    field.setAccessible(true);

    Object defaultValue;
    try {
      defaultValue = field.get(this);
    } catch (IllegalAccessException e) {
      field.setAccessible(accessible);
      throw new IllegalStateException("Cannot get the default value from " + field.getName(), e);
    }

    if (!contains(path)) {
      super.set(path, converter.convertToRaw(defaultValue));
    } else {
      try {
        field.set(this, converter.convert(this.getNormalized(path)));
        field.setAccessible(accessible);
      } catch (IllegalAccessException e) {
        field.setAccessible(accessible);
        throw new IllegalStateException("Cannot set the value for " + field.getName(), e);
      }
    }
  }

  private void checkAndSetField(Field field, ConfigPath configPath, Object value) {
    String path = configPath.value();
    Converter converter = createConverterSafe(configPath.converter());
    super.set(path, converter.convertToRaw(value));
    boolean accessible = field.isAccessible();
    field.setAccessible(true);
    try {
      field.set(this, value);
      field.setAccessible(accessible);
    } catch (IllegalAccessException e) {
      field.setAccessible(accessible);
      throw new IllegalStateException("Cannot set the value for " + field.getName(), e);
    }
  }
}
