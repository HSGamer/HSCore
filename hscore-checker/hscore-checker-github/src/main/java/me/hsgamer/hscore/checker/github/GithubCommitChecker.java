package me.hsgamer.hscore.checker.github;

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
 * The latest commit checker for the GitHub repository
 */
public class GithubCommitChecker implements VersionChecker {
  private final String url;

  /**
   * Create a new checker
   *
   * @param repo   the repository
   * @param branch the branch
   */
  public GithubCommitChecker(String repo, String branch) {
    this.url = "https://api.github.com/repos/" + repo + "/commits/heads/" + branch;
  }

  @Override
  public @NotNull CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try (InputStream inputStream = UserAgent.FIREFOX.assignToConnection(WebUtils.createConnection(url)).getInputStream()) {
        JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
        return jsonObject.getString("sha");
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    });
  }
}
