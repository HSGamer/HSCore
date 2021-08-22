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
import java.util.logging.Level;
import java.util.logging.Logger;

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
  /**
   * The logger
   */
  protected static final Logger LOGGER = Logger.getLogger("Downloader");

  protected final Map<String, DownloadInfo> downloadInfoMap = new ConcurrentHashMap<>();
  private final String dbUrl;
  private final File folder;

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
   * Load download infos
   */
  public CompletableFuture<Map<String, DownloadInfo>> loadDownloadsInfo() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return new JSONParser().parse(new InputStreamReader(WebUtils.openConnection(dbUrl, UserAgent.FIREFOX).getInputStream()));
      } catch (IOException | ParseException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when getting the addon info");
        return null;
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
      if (throwable != null) {
        return;
      }
      downloadInfoMap.clear();
      downloadInfoMap.putAll(map);
    });
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
}
