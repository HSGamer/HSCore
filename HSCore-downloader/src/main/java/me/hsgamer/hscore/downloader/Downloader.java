package me.hsgamer.hscore.downloader;

import me.hsgamer.hscore.web.WebUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The downloader
 */
public class Downloader {
  /**
   * The logger
   */
  protected static final Logger LOGGER = Logger.getLogger("Downloader");

  private final Map<String, DownloadInfo> downloadInfoMap = new HashMap<>();
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
    loadDownloadsInfo();
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
  private void loadDownloadsInfo() {
    CompletableFuture.supplyAsync(() -> {
      try {
        return WebUtils.getJSONFromURL(dbUrl);
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
