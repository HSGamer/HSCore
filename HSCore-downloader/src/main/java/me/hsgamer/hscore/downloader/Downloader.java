package me.hsgamer.hscore.downloader;

import me.hsgamer.hscore.downloader.object.DownloadInfo;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
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
  public void loadDownloadsInfo() {
    downloadInfoMap.clear();
    CompletableFuture.supplyAsync(() -> {
      try {
        return WebUtils.getJSONFromURL(dbUrl, UserAgent.FIREFOX);
      } catch (IOException | ParseException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when getting the addon info");
        return null;
      }
    }).thenAccept(jsonObject -> {
      if (!(jsonObject instanceof JSONObject)) {
        return;
      }

      ((JSONObject) jsonObject).forEach((key, raw) -> {
        if (!(raw instanceof JSONObject)) {
          return;
        }

        JSONObject value = (JSONObject) raw;
        String name = String.valueOf(key);

        downloadInfoMap.put(name, new DownloadInfo(name, value, this));
      });
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
