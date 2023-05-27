package me.hsgamer.hscore.checker.github;

import com.google.gson.JsonArray;
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
 * The release version checker for the GitHub repository
 */
public class GithubReleaseChecker implements VersionChecker {
  private final String url;
  private final UserAgent userAgent;

  /**
   * Create a new checker
   *
   * @param repo      the repository
   * @param userAgent the user agent
   */
  public GithubReleaseChecker(String repo, UserAgent userAgent) {
    this.url = "https://api.github.com/repos/" + repo + "/releases?per_page=1";
    this.userAgent = userAgent;
  }

  /**
   * Create a new checker
   *
   * @param repo the repository
   */
  public GithubReleaseChecker(String repo) {
    this(repo, UserAgent.FIREFOX);
  }

  @Override
  public @NotNull CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try (
        InputStream inputStream = WebUtils.createConnection(url, userAgent::assignToConnection).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream)
      ) {
        JsonElement element = GsonUtils.parse(reader);
        if (!element.isJsonArray()) {
          throw new IOException("The response is not a JSON array");
        }
        JsonArray array = element.getAsJsonArray();
        if (GsonUtils.isEmpty(array)) {
          throw new IOException("The response is empty");
        }
        JsonElement first = array.get(0);
        if (!first.isJsonObject()) {
          throw new IOException("The first element is not a JSON object");
        }
        JsonElement tag = first.getAsJsonObject().get("tag_name");
        if (tag == null) {
          throw new IOException("The tag name is null");
        }
        return tag.getAsString();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
