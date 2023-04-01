package me.hsgamer.hscore.bukkit.simpleplugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class BukkitPostEnableListener extends PostEnableListener {
  public BukkitPostEnableListener(Plugin plugin) {
    super(plugin);
  }

  @Override
  public void setup() {
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::executePostEnableFunctions);
  }
}
