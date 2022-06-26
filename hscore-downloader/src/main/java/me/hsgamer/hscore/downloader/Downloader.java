package me.hsgamer.hscore.downloader;

import me.hsgamer.hscore.downloader.loader.DownloadInfoLoader;
import me.hsgamer.hscore.downloader.loader.JsonDownloadInfoLoader;
import me.hsgamer.hscore.downloader.object.DownloadInfo;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The downloader
 */
public class Downloader {

  protected final Map<String, DownloadInfo> downloadInfoMap = new ConcurrentHashMap<>();
  private final DownloadInfoLoader loader;
  private final File folder;

  /**
   * Create a new downloader
   *
   * @param loader the loader
   * @param folder the folder to save downloaded files
   */
  public Downloader(DownloadInfoLoader loader, File folder) {
    this.loader = loader;
    this.folder = folder;
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  /**
   * Create a new downloader
   *
   * @param dbUrl  the database url
   * @param folder the folder to save downloaded files
   */
  public Downloader(String dbUrl, File folder) {
    this(new JsonDownloadInfoLoader(dbUrl), folder);
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
   * Load download infos
   */
  public CompletableFuture<Map<String, DownloadInfo>> loadDownloadsInfo() {
    return loader.load(this).whenCompleteAsync((map, throwable) -> {
      if (map == null) {
        return;
      }
      downloadInfoMap.clear();
      downloadInfoMap.putAll(map);
    });
  }
}
