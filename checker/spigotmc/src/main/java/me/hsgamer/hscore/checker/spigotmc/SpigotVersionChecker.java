package me.hsgamer.hscore.checker.spigotmc;

import com.google.gson.JsonElement;
import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.gson.GsonUtils;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

/**
 * A simple version checker for SpigotMC
 */
public final class SpigotVersionChecker implements VersionChecker {
  private final int resourceId;
  private final UserAgent userAgent;

  /**
   * Create a version checker
   *
   * @param resourceId the resource id from SpigotMC
   * @param userAgent  the user agent
   */
  public SpigotVersionChecker(final int resourceId, UserAgent userAgent) {
    this.resourceId = resourceId;
    this.userAgent = userAgent;
  }

  /**
   * Create a version checker
   *
   * @param resourceId the resource id from SpigotMC
   */
  public SpigotVersionChecker(final int resourceId) {
    this(resourceId, UserAgent.FIREFOX);
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
        InputStream inputStream = WebUtils.createConnection("https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=" + resourceId, userAgent::assignToConnection).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream)
      ) {
        JsonElement element = GsonUtils.parse(reader);
        if (!element.isJsonObject()) {
          throw new IOException("Invalid JSON");
        }
        JsonElement currentVersion = element.getAsJsonObject().get("current_version");
        if (currentVersion == null) {
          throw new IOException("Cannot get the plugin version");
        }
        return currentVersion.getAsString();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
  }
}