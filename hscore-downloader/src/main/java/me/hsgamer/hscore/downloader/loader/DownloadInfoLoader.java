package me.hsgamer.hscore.downloader.loader;

import me.hsgamer.hscore.downloader.Downloader;
import me.hsgamer.hscore.downloader.object.DownloadInfo;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The loader for {@link DownloadInfo}
 */
public interface DownloadInfoLoader {
  /**
   * Load the {@link DownloadInfo}
   *
   * @param downloader the {@link Downloader}
   *
   * @return the map of {@link DownloadInfo}
   */
  CompletableFuture<Map<String, DownloadInfo>> load(Downloader downloader);
}
