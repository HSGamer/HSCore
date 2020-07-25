package me.hsgamer.hscore.bukkit.updater;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import me.hsgamer.hscore.web.WebUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * A simple version checker for Spigot
 */
public final class VersionChecker {

  private final int resourceId;

  /**
   * Create a version checker
   *
   * @param resourceId the resource id from SpigotMC
   */
  public VersionChecker(int resourceId) {
    this.resourceId = resourceId;
  }

  /**
   * Get the version of the resource
   *
   * @return the version
   */
  public CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        JSONObject object = WebUtils.getJSONFromURL(
            "https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=" + resourceId);
        if (!object.containsKey("current_version")) {
          throw new IOException("Cannot get the plugin version");
        }
        return String.valueOf(object.get("current_version"));
      } catch (IOException | ParseException exception) {
        return "Error when getting version: " + exception.getMessage();
      }
    });
  }
}