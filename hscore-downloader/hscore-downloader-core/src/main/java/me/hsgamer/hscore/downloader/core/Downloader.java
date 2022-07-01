package me.hsgamer.hscore.downloader.core;

import me.hsgamer.hscore.downloader.core.loader.DownloadInfoLoader;
import me.hsgamer.hscore.downloader.core.loader.InputStreamLoader;
import me.hsgamer.hscore.downloader.core.object.DownloadInfo;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The downloader
 */
public class Downloader {
  private static final Logger logger = Logger.getLogger(Downloader.class.getSimpleName());
  private final Map<String, DownloadInfo> downloadInfoMap = new ConcurrentHashMap<>();
  private final DownloadInfoLoader downloadInfoLoader;
  private final InputStreamLoader inputStreamLoader;
  private final File folder;

  /**
   * Create a new downloader
   *
   * @param downloadInfoLoader the loader
   * @param folder             the folder to save downloaded files
   */
  public Downloader(DownloadInfoLoader downloadInfoLoader, InputStreamLoader inputStreamLoader, File folder) {
    this.downloadInfoLoader = downloadInfoLoader;
    this.inputStreamLoader = inputStreamLoader;
    this.folder = folder;
    if (!folder.exists() && folder.mkdirs()) {
      logger.info(() -> "Create folder: " + folder.getAbsolutePath());
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
   * Get the download info loader
   *
   * @return the download info loader
   */
  public DownloadInfoLoader getDownloadInfoLoader() {
    return downloadInfoLoader;
  }

  /**
   * Get the input stream loader
   *
   * @return the input stream loader
   */
  public InputStreamLoader getInputStreamLoader() {
    return inputStreamLoader;
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
   * Load download infos
   */
  public CompletableFuture<Map<String, DownloadInfo>> loadDownloadsInfo() {
    return downloadInfoLoader.load(this).whenCompleteAsync((map, throwable) -> {
      if (throwable != null) {
        logger.log(Level.WARNING, "A throwable occurred when loading download info", throwable);
      }
      if (map == null) {
        return;
      }
      downloadInfoMap.clear();
      downloadInfoMap.putAll(map);
    });
  }
}
