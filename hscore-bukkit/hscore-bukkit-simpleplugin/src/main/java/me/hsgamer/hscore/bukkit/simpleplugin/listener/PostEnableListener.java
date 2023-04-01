package me.hsgamer.hscore.bukkit.simpleplugin.listener;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class PostEnableListener {
  protected final Plugin plugin;
  private final List<Runnable> postEnableFunctions;

  protected PostEnableListener(Plugin plugin) {
    this.plugin = plugin;
    this.postEnableFunctions = new ArrayList<>();
  }

  public void addPostEnableRunnable(Runnable runnable) {
    postEnableFunctions.add(runnable);
  }

  protected void executePostEnableFunctions() {
    postEnableFunctions.forEach(Runnable::run);
    postEnableFunctions.clear();
  }

  public abstract void setup();
}
