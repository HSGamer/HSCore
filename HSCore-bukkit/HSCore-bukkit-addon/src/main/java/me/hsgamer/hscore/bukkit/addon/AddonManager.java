package me.hsgamer.hscore.bukkit.addon;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import me.hsgamer.hscore.bukkit.addon.object.Addon;
import me.hsgamer.hscore.bukkit.addon.object.AddonClassLoader;
import me.hsgamer.hscore.bukkit.addon.object.AddonDescription;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: JavaDocs
public abstract class AddonManager {

  private final Map<String, Addon> addons = new LinkedHashMap<>();
  private final Map<Addon, AddonClassLoader> loaderMap = new HashMap<>();
  private final File addonsDir;
  private final JavaPlugin plugin;

  public AddonManager(JavaPlugin plugin) {
    this.plugin = plugin;
    addonsDir = new File(plugin.getDataFolder(), "addon");
    if (!addonsDir.exists()) {
      addonsDir.mkdirs();
    }
  }

  public JavaPlugin getPlugin() {
    return plugin;
  }

  public File getAddonsDir() {
    return addonsDir;
  }

  private AddonDescription getAddonDescription(JarFile jar)
      throws IOException, InvalidConfigurationException {
    // Load addon.yml file
    JarEntry entry = jar.getJarEntry("addon.yml");
    if (entry == null) {
      throw new NoSuchFileException(
          "Addon '" + jar.getName() + "' doesn't contain addon.yml file");
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(jar.getInputStream(entry)));
    YamlConfiguration data = new YamlConfiguration();
    data.load(reader);

    // Load required descriptions
    String name = data.getString("name");
    String version = data.getString("version");
    String mainClass = data.getString("main");
    if (name == null) {
      throw new InvalidConfigurationException(
          "Addon '" + jar.getName() + "' doesn't have a name on addon.yml");
    }
    if (version == null) {
      throw new InvalidConfigurationException(
          "Addon '" + jar.getName() + "' doesn't have a version on addon.yml");
    }
    if (mainClass == null) {
      throw new InvalidConfigurationException(
          "Addon '" + jar.getName() + "' doesn't have a main class on addon.yml");
    }
    return new AddonDescription(name, version, mainClass, data);
  }

  public void loadAddons() {
    Map<String, Addon> addonMap = new HashMap<>();

    // Load the addon files
    for (File file : Objects.requireNonNull(addonsDir.listFiles())) {
      if (file.isFile() && file.getName().endsWith(".jar")) {
        try (JarFile jar = new JarFile(file)) {
          // Get addon description
          AddonDescription addonDescription = getAddonDescription(jar);
          if (addonMap.containsKey(addonDescription.getName())) {
            plugin.getLogger().warning("Duplicated addon " + addonDescription.getName());
            continue;
          }

          // Try to load the addon
          AddonClassLoader loader = new AddonClassLoader(this, file, addonDescription,
              getClass().getClassLoader());
          Addon addon = loader.getAddon();
          onAddonLoading(addon);

          addonMap.put(addonDescription.getName(), loader.getAddon());
          loaderMap.put(addon, loader);
        } catch (InvalidConfigurationException e) {
          plugin.getLogger().log(Level.WARNING, e.getMessage(), e);
        } catch (Exception e) {
          plugin.getLogger().log(Level.WARNING, "Error when loading jar", e);
        }
      }
    }

    // Sort and load the addons
    addonMap = sortAddons(addonMap);
    Map<String, Addon> finalAddons = new LinkedHashMap<>();
    addonMap.forEach((key, addon) -> {
      try {
        if (!addon.onLoad()) {
          plugin.getLogger().warning(
              "Failed to load " + key + " " + addon.getDescription().getVersion());
          closeClassLoader(addon);
          return;
        }

        plugin.getLogger()
            .info("Loaded " + key + " " + addon.getDescription().getVersion());
        finalAddons.put(key, addon);
      } catch (Throwable t) {
        plugin.getLogger().log(Level.WARNING, t, () -> "Error when loading " + key);
        closeClassLoader(addon);
      }
    });

    // Store the final addons map
    addons.putAll(finalAddons);
  }

  public boolean enableAddon(String name, boolean closeLoaderOnFailed) {
    Addon addon = addons.get(name);
    try {
      addon.onEnable();
      return true;
    } catch (Throwable t) {
      plugin.getLogger().log(Level.WARNING, t, () -> "Error when enabling " + name);
      if (closeLoaderOnFailed) {
        closeClassLoader(addon);
      }
      return false;
    }
  }

  public boolean disableAddon(String name, boolean closeLoaderOnFailed) {
    Addon addon = addons.get(name);
    try {
      addon.onDisable();
      return true;
    } catch (Throwable t) {
      plugin.getLogger().log(Level.WARNING, t, () -> "Error when disabling " + name);
      if (closeLoaderOnFailed) {
        closeClassLoader(addon);
      }
      return false;
    }
  }

  public void enableAddons() {
    List<String> failed = new ArrayList<>();
    addons.keySet().forEach(name -> {
      if (!enableAddon(name, true)) {
        failed.add(name);
      } else {
        plugin.getLogger().log(Level.INFO, "Enabled {0}",
            String.join(" ", name, addons.get(name).getDescription().getVersion()));
      }
    });
    failed.forEach(addons::remove);
  }

  public void callPostEnable() {
    addons.values().forEach(Addon::onPostEnable);
  }

  public void callReload() {
    addons.values().forEach(Addon::onReload);
  }

  public void disableAddons() {
    addons.keySet().forEach(name -> {
      if (disableAddon(name, false)) {
        plugin.getLogger().log(Level.INFO, "Disabled {0}",
            String.join(" ", name, addons.get(name).getDescription().getVersion()));
      }
    });

    addons.values().forEach(this::closeClassLoader);
    addons.clear();
  }

  private void closeClassLoader(Addon addon) {
    loaderMap.computeIfPresent(addon, (a, loader) -> {
      try {
        loader.close();
      } catch (IOException e) {
        plugin.getLogger().log(Level.WARNING, "Error when closing ClassLoader", e);
      }
      return null;
    });
  }

  public Addon getAddon(String name) {
    return addons.get(name);
  }

  public boolean isAddonLoaded(String name) {
    return addons.containsKey(name);
  }

  public Map<String, Addon> getLoadedAddons() {
    return addons;
  }

  protected abstract Map<String, Addon> sortAddons(Map<String, Addon> original);

  protected void onAddonLoading(Addon addon) {
    // EMPTY
  }

  public Class<?> findClass(Addon addon, String name) {
    for (AddonClassLoader loader : loaderMap.values()) {
      if (loaderMap.containsKey(addon)) {
        continue;
      }
      Class<?> clazz = loader.findClass(name, false);
      if (clazz != null) {
        return clazz;
      }
    }
    return null;
  }

  public Map<String, Integer> getAddonCount() {
    Map<String, Integer> map = new HashMap<>();
    Set<String> list = addons.keySet();
    if (list.isEmpty()) {
      map.put("Empty", 1);
    } else {
      list.forEach(s -> map.put(s, 1));
    }
    return map;
  }
}
