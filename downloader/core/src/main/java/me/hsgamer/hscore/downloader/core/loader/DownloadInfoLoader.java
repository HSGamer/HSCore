package me.hsgamer.hscore.downloader.core.loader;

import me.hsgamer.hscore.downloader.core.Downloader;
import me.hsgamer.hscore.downloader.core.object.DownloadInfo;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The {@link DownloadInfo} loader
 */
public interface DownloadInfoLoader {
  /**
   * Load the {@link DownloadInfo}
   *
   * @param downloader the downloader
   *
   * @return the {@link DownloadInfo}
   */
  CompletableFuture<Map<String, DownloadInfo>> load(Downloader downloader);
}
