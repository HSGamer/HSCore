package me.hsgamer.hscore.checker.modrinth;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A version checker for Modrinth
 */
public class ModrinthVersionChecker implements VersionChecker {
  private final String id;
  private final List<String> loaders;
  private final List<String> gameVersions;
  private final boolean featured;
  private final UserAgent userAgent;

  /**
   * Create a version checker
   *
   * @param id           the id / slug of the project
   * @param loaders      the loaders to filter for
   * @param gameVersions the game versions to filter for
   * @param featured     whether to only show featured versions
   * @param userAgent    the user agent
   */
  public ModrinthVersionChecker(String id, List<String> loaders, List<String> gameVersions, boolean featured, UserAgent userAgent) {
    this.id = id;
    this.loaders = loaders;
    this.gameVersions = gameVersions;
    this.featured = featured;
    this.userAgent = userAgent;
  }

  /**
   * Create a version checker
   *
   * @param id           the id / slug of the project
   * @param loaders      the loaders to filter for
   * @param gameVersions the game versions to filter for
   * @param featured     whether to only show featured versions
   */
  public ModrinthVersionChecker(String id, List<String> loaders, List<String> gameVersions, boolean featured) {
    this(id, loaders, gameVersions, featured, new UserAgent(id));
  }

  /**
   * Create a version checker
   *
   * @param id        the id / slug of the project
   * @param userAgent the user agent
   */
  public ModrinthVersionChecker(String id, UserAgent userAgent) {
    this(id, Collections.emptyList(), Collections.emptyList(), true, userAgent);
  }

  /**
   * Create a version checker
   *
   * @param id the id / slug of the project
   */
  public ModrinthVersionChecker(String id) {
    this(id, new UserAgent(id));
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull CompletableFuture<String> getVersion() {
    StringBuilder urlBuilder = new StringBuilder("https://api.modrinth.com/api/v1/mod/" + id + "/version");
    urlBuilder.append("?loaders=[").append(String.join(",", loaders)).append("]");
    urlBuilder.append("&game_versions=[").append(String.join(",", gameVersions)).append("]");
    urlBuilder.append("&featured=").append(featured);
    return CompletableFuture.supplyAsync(() -> {
      try (
        InputStream inputStream = WebUtils.createConnection(urlBuilder.toString(), userAgent::assignToConnection).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream)
      ) {
        JsonElement element = new JsonParser().parse(reader);
        if (!element.isJsonArray()) {
          throw new IOException("Invalid JSON");
        }
        JsonArray array = element.getAsJsonArray();
        if (array.isEmpty()) {
          throw new IOException("The response is empty");
        }
        JsonElement first = array.get(0);
        if (!first.isJsonObject()) {
          throw new IOException("The first element is not a JSON object");
        }
        JsonElement versionElement = first.getAsJsonObject().get("version_number");
        if (versionElement == null) {
          throw new IOException("The version number is not a primitive");
        }
        return versionElement.getAsString();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
