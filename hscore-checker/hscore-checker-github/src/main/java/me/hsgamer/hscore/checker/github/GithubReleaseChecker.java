package me.hsgamer.hscore.checker.github;

import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
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
      try (InputStream inputStream = userAgent.assignToConnection(WebUtils.createConnection(url)).getInputStream()) {
        JSONArray array = new JSONArray(new JSONTokener(inputStream));
        JSONObject object = array.getJSONObject(0);
        return object.getString("tag_name");
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
