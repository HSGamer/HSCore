package me.hsgamer.hscore.downloader.core;

import me.hsgamer.hscore.downloader.core.loader.DownloadInfoLoader;
import me.hsgamer.hscore.downloader.core.loader.InputStreamLoader;
import me.hsgamer.hscore.downloader.core.object.DownloadInfo;
import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.provider.LoggerProvider;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The downloader
 */
public class Downloader {
  private static final Logger logger = LoggerProvider.getLogger(Downloader.class);
  private final Map<String, DownloadInfo> downloadInfoMap = new ConcurrentHashMap<>();
  private final AtomicBoolean isLoaded = new AtomicBoolean(false);
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
      logger.log("Create folder: " + folder.getAbsolutePath());
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
   * Get the loaded download infos
   *
   * @return the loaded download infos
   */
  public Map<String, DownloadInfo> getLoadedDownloadInfo() {
    return Collections.unmodifiableMap(downloadInfoMap);
  }

  /**
   * Check if the downloader finished loading the download infos
   *
   * @return true if it did
   */
  public boolean isLoaded() {
    return isLoaded.get();
  }

  /**
   * Set up the downloader
   */
  public void setup() {
    isLoaded.set(false);
    downloadInfoMap.clear();
    downloadInfoLoader.load(this).whenCompleteAsync((map, throwable) -> {
      if (throwable != null) {
        logger.log(LogLevel.WARN, "A throwable occurred when loading download info", throwable);
      }
      if (map != null) {
        downloadInfoMap.putAll(map);
      }
      isLoaded.set(true);
      onLoaded();
    });
  }

  /**
   * Call when the downloader is finished loading the download infos
   */
  public void onLoaded() {
    // EMPTY
  }
}
