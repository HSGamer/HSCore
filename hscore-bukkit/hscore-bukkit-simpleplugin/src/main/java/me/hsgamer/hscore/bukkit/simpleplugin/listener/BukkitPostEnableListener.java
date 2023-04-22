package me.hsgamer.hscore.bukkit.simpleplugin.listener;

import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import org.bukkit.plugin.Plugin;

public class BukkitPostEnableListener extends PostEnableListener {
  public BukkitPostEnableListener(Plugin plugin) {
    super(plugin);
  }

  @Override
  public void setup() {
    Scheduler.plugin(plugin).sync().runTask(this::executePostEnableFunctions);
  }
}
