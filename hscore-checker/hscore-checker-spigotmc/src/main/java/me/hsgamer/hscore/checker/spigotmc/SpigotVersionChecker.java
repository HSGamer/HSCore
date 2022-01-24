package me.hsgamer.hscore.checker.spigotmc;

import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
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
  @Override
  public @NotNull CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try (
        InputStream inputStream = UserAgent.FIREFOX.assignToConnection(
          WebUtils.createConnection("https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=" + resourceId)
        ).getInputStream()
      ) {
        JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
        if (!jsonObject.has("current_version")) {
          throw new IOException("Cannot get the plugin version");
        }
        return jsonObject.getString("current_version");
      } catch (IOException exception) {
        throw new IllegalStateException(exception);
      }
    });
  }
}