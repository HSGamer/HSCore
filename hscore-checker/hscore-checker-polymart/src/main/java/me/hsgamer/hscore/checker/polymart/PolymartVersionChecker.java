package me.hsgamer.hscore.checker.polymart;

import me.hsgamer.hscore.checker.VersionChecker;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * A simple version checker for Polymart
 */
public class PolymartVersionChecker implements VersionChecker {
  private final int resourceId;
  private final UserAgent userAgent;

  /**
   * Create a version checker
   *
   * @param resourceId the resource id from Polymart
   * @param userAgent  the user agent
   */
  public PolymartVersionChecker(final int resourceId, UserAgent userAgent) {
    this.resourceId = resourceId;
    this.userAgent = userAgent;
  }

  /**
   * Create a version checker
   *
   * @param resourceId the resource id from Polymart
   */
  public PolymartVersionChecker(final int resourceId) {
    this(resourceId, UserAgent.FIREFOX);
  }

  @Override
  public @NotNull CompletableFuture<String> getVersion() {
    return CompletableFuture.supplyAsync(() -> {
      try (
        InputStream inputStream = WebUtils.createConnection("https://api.polymart.org/v1/getResourceInfoSimple/?key=version&resource_id=" + resourceId, userAgent::assignToConnection).getInputStream();
        ByteArrayOutputStream result = new ByteArrayOutputStream()
      ) {
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
          result.write(buffer, 0, length);
        }
        return new String(result.toByteArray(), StandardCharsets.UTF_8).trim();
      } catch (IOException exception) {
        throw new IllegalStateException(exception);
      }
    });
  }
}
