package me.hsgamer.hscore.checker.github;

import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * The release version checker for the GitHub repository
 */
public class GithubReleaseChecker implements VersionChecker {
  private final JSONParser parser = new JSONParser();
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
        InputStream inputStream = userAgent.assignToConnection(WebUtils.createConnection(url)).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream)
      ) {
        Object object = parser.parse(reader);
        if (!(object instanceof JSONArray)) {
          throw new IOException("The response is not a JSON array");
        }
        JSONArray array = (JSONArray) object;
        if (array.isEmpty()) {
          throw new IOException("The response is empty");
        }
        Object first = array.get(0);
        if (!(first instanceof JSONObject)) {
          throw new IOException("The first element is not a JSON object");
        }
        JSONObject jsonObject = (JSONObject) first;
        return Objects.toString(jsonObject.get("tag_name"));
      } catch (IOException | ParseException e) {
        throw new CompletionException(e);
      }
    });
  }
}
