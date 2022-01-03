package me.hsgamer.hscore.downloader;

import me.hsgamer.hscore.downloader.object.DownloadInfo;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The downloader <br>
 * The format of the database:
 * <pre>
 *   {@code
 *   {
 *     "name1": {
 *       "file-name": "name1.jar",
 *       "version": "1.0",
 *       "direct-link": "example.com/name1.jar",
 *       ...
 *     },
 *     "name2": {
 *       "file-name": "name2.jar",
 *       "version": "1.0-SN",
 *       "direct-link": "example.com/name2.jar",
 *       ...
 *     },
 *     ...
 *   }
 *   }
 * </pre>
 */
public class Downloader {

  protected final Map<String, DownloadInfo> downloadInfoMap = new ConcurrentHashMap<>();
  private final String dbUrl;
  private final File folder;
  private final AtomicReference<UserAgent> userAgentPreference = new AtomicReference<>(UserAgent.FIREFOX);

  /**
   * Create a new downloader
   *
   * @param dbUrl  the database url
   * @param folder the folder to save downloaded files
   */
  public Downloader(String dbUrl, File folder) {
    this.dbUrl = dbUrl;
    this.folder = folder;
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  /**
   * Get the folder
   *
   * @return the folder
   */
  public File getFolder() {
    return folder;
  }

  /**
   * Get the download info
   *
   * @param name the name
   *
   * @return the download info
   */
  public Optional<DownloadInfo> getDownloadInfo(String name) {
    return Optional.ofNullable(downloadInfoMap.get(name));
  }

  /**
   * Get the user agent the downloader is using
   *
   * @return the user agent
   */
  public UserAgent getUserAgent() {
    return userAgentPreference.get();
  }

  /**
   * Set the user agent
   *
   * @param userAgent the user agent
   */
  public void setUserAgent(UserAgent userAgent) {
    userAgentPreference.set(userAgent);
  }

  /**
   * Load download infos
   */
  public CompletableFuture<Map<String, DownloadInfo>> loadDownloadsInfo() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return new InputStreamReader(userAgentPreference.get().assignToConnection(WebUtils.createConnection(dbUrl)).getInputStream());
      } catch (IOException e) {
        throw new IllegalStateException("Something wrong when getting the info", e);
      }
    }).thenApplyAsync(inputStreamReader -> {
      try {
        return new JSONParser().parse(inputStreamReader);
      } catch (IOException | ParseException e) {
        throw new IllegalArgumentException("Something wrong when parsing the info", e);
      }
    }).<Map<String, DownloadInfo>>thenApplyAsync(jsonObject -> {
      if (!(jsonObject instanceof JSONObject)) {
        return Collections.emptyMap();
      }
      Map<String, DownloadInfo> map = new HashMap<>();
      // noinspection unchecked
      ((JSONObject) jsonObject).forEach((key, raw) -> {
        if (!(raw instanceof JSONObject)) {
          return;
        }
        JSONObject value = (JSONObject) raw;
        String name = String.valueOf(key);
        map.put(name, new DownloadInfo(name, value, this));
      });
      return map;
    }).whenCompleteAsync((map, throwable) -> {
      if (map == null) {
        return;
      }
      downloadInfoMap.clear();
      downloadInfoMap.putAll(map);
    });
  }
}
