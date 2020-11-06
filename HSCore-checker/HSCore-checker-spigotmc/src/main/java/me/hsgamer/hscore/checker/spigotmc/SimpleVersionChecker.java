package me.hsgamer.hscore.checker.spigotmc;

import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * A simple version checker for SpigotMC
 */
public final class SimpleVersionChecker {

  private final int resourceId;

  /**
   * Create a version checker
   *
   * @param resourceId the resource id from SpigotMC
   */
  public SimpleVersionChecker(final int resourceId) {
    this.resourceId = resourceId;
  }

  /**
   * Get the version of the resource
   *
   * @return the version
   */
  @NotNull
  public final CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        JSONObject object = (JSONObject) WebUtils.getJSONFromURL(
          "https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=" + resourceId, UserAgent.FIREFOX);
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