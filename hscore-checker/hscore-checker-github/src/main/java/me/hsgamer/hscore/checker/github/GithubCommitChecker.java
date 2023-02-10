package me.hsgamer.hscore.checker.github;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

/**
 * The latest commit checker for the GitHub repository
 */
public class GithubCommitChecker implements VersionChecker {
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
        InputStream inputStream = WebUtils.createConnection(url, userAgent::assignToConnection).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream)
      ) {
        JsonElement element = JsonParser.parseReader(reader);
        if (!element.isJsonObject()) {
          throw new IOException("The response is null");
        }
        JsonElement sha = element.getAsJsonObject().get("sha");
        if (sha == null) {
          throw new IOException("The response is null");
        }
        return sha.getAsString();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
