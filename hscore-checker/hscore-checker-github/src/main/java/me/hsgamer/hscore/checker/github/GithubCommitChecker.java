package me.hsgamer.hscore.checker.github;

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
import java.util.concurrent.CompletionException;

/**
 * The latest commit checker for the GitHub repository
 */
public class GithubCommitChecker implements VersionChecker {
  private final JSONParser parser = new JSONParser();
  private final String url;
  private final UserAgent userAgent;

  /**
   * Create a new checker
   *
   * @param repo      the repository
   * @param branch    the branch
   * @param userAgent the user agent
   */
  public GithubCommitChecker(String repo, String branch, UserAgent userAgent) {
    this.userAgent = userAgent;
    this.url = "https://api.github.com/repos/" + repo + "/commits/heads/" + branch;
  }

  /**
   * Create a new checker
   *
   * @param repo   the repository
   * @param branch the branch
   */
  public GithubCommitChecker(String repo, String branch) {
    this(repo, branch, UserAgent.FIREFOX);
  }

  @Override
  public @NotNull CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try (
        InputStream inputStream = userAgent.assignToConnection(WebUtils.createConnection(url)).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream)
      ) {
        Object object = parser.parse(reader);
        if (!(object instanceof JSONObject)) {
          throw new IOException("The response is not a JSON object");
        }
        JSONObject jsonObject = (JSONObject) object;
        return Objects.toString(jsonObject.get("sha"));
      } catch (IOException | ParseException e) {
        throw new CompletionException(e);
      }
    });
  }
}
