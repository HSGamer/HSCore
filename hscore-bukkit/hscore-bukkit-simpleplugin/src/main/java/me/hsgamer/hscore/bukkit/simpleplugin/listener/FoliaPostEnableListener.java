package me.hsgamer.hscore.bukkit.simpleplugin.listener;

import io.papermc.paper.threadedregions.RegionizedServerInitEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class FoliaPostEnableListener extends PostEnableListener implements Listener {
  public FoliaPostEnableListener(Plugin plugin) {
    super(plugin);
  }

  @Override
  public void setup() {
    Bukkit.getPluginManager().registerEvents(this, this.plugin);
  }

  @EventHandler
  public void onServerInit(RegionizedServerInitEvent event) {
    this.executePostEnableFunctions();
  }
}
