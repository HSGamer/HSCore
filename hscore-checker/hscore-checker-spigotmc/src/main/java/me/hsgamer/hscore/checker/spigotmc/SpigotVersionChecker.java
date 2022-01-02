package me.hsgamer.hscore.checker.spigotmc;

import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

/**
 * A simple version checker for SpigotMC
 */
public final class SpigotVersionChecker implements VersionChecker {

  private final int resourceId;

  /**
   * Create a version checker
   *
   * @param resourceId the resource id from SpigotMC
   */
  public SpigotVersionChecker(final int resourceId) {
    this.resourceId = resourceId;
  }

  /**
   * Get the version of the resource
   *
   * @return the version
   */
  @NotNull
  public CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(WebUtils.openConnection("https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=" + resourceId, UserAgent.FIREFOX).getInputStream()));
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