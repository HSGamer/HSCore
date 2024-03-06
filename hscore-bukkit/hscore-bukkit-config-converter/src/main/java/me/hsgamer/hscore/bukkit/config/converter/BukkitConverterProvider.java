package me.hsgamer.hscore.bukkit.config.converter;

import me.hsgamer.hscore.config.annotation.converter.Converter;
import me.hsgamer.hscore.config.annotation.converter.ConverterProvider;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Optional;

/**
 * The {@link ConverterProvider} for Bukkit
 */
public class BukkitConverterProvider implements ConverterProvider {
  /**
   * Register the provider
   */
  public static void register() {
    DefaultConverterManager.registerProvider(new BukkitConverterProvider());
  }

  @Override
  public Optional<Converter> getConverter(Class<?> type) {
    return ConfigurationSerializable.class.isAssignableFrom(type) ? Optional.of(new BukkitConverter(type)) : Optional.empty();
  }
}
