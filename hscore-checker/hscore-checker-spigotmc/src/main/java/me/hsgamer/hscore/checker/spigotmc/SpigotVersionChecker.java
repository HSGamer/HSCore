package me.hsgamer.hscore.checker.spigotmc;

import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A simple version checker for SpigotMC
 */
public final class SpigotVersionChecker implements VersionChecker {
  private final JSONParser parser = new JSONParser();
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
        InputStream inputStream = userAgent.assignToConnection(
          WebUtils.createConnection("https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=" + resourceId)
        ).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream)
      ) {
        Object object = parser.parse(reader);
        if (!(object instanceof JSONObject)) {
          throw new IOException("Invalid JSON");
        }
        JSONObject jsonObject = (JSONObject) object;
        if (!jsonObject.containsKey("current_version")) {
          throw new IOException("Cannot get the plugin version");
        }
        return Objects.toString(jsonObject.get("current_version"));
      } catch (IOException | ParseException exception) {
        throw new IllegalStateException(exception);
      }
    });
  }
}