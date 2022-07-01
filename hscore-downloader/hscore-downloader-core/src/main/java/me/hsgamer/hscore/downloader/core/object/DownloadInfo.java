package me.hsgamer.hscore.downloader.core.object;

import me.hsgamer.hscore.downloader.core.Downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The download information
 */
public final class DownloadInfo {
  private final String name;
  private final String fileName;
  private final String version;
  private final String directLink;
  private final Map<String, Object> data;
  private final Downloader downloader;
  private final AtomicBoolean isDownloading = new AtomicBoolean(false);
  private CompletableFuture<File> currentDownloadTask = null;

  /**
   * Create a new download information
   *
   * @param name       the name
   * @param fileName   the file name
   * @param version    the version
   * @param directLink the direct link
   * @param data       the data
   * @param downloader the downloader
   */
  public DownloadInfo(String name, String fileName, String version, String directLink, Map<String, Object> data, Downloader downloader) {
    this.name = name;
    this.fileName = fileName;
    this.version = version;
    this.directLink = directLink;
    this.data = data;
    this.downloader = downloader;
  }

  /**
   * Create a new download information
   *
   * @param name       the name
   * @param fileName   the file name
   * @param version    the version
   * @param directLink the direct link
   * @param downloader the downloader
   */
  public DownloadInfo(String name, String fileName, String version, String directLink, Downloader downloader) {
    this(name, fileName, version, directLink, Collections.emptyMap(), downloader);
  }

  /**
   * Get the name
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the version
   *
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Get the file name
   *
   * @return the file name
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Get the direct link
   *
   * @return the direct link
   */
  public String getDirectLink() {
    return directLink;
  }

  /**
   * Get the data
   *
   * @return the data
   */
  public Map<String, Object> getData() {
    return data;
  }

  /**
   * Check if the download is running
   *
   * @return true if the download is running
   */
  public boolean isDownloading() {
    return isDownloading.get();
  }

  /**
   * Download the file.
   * If the download is running, it will return the current download task.
   */
  public CompletableFuture<File> download() {
    if (currentDownloadTask != null && !currentDownloadTask.isDone()) {
      return currentDownloadTask;
    }

    currentDownloadTask = CompletableFuture.supplyAsync(() -> {
      isDownloading.set(true);
      File file = new File(downloader.getFolder(), fileName);
      try (
        ReadableByteChannel readableByteChannel = Channels.newChannel(downloader.getInputStreamLoader().load(this));
        FileOutputStream fileOutputStream = new FileOutputStream(file)
      ) {
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        return file;
      } catch (IOException e) {
        throw new CompletionException(e);
      } finally {
        isDownloading.set(false);
      }
    });
    return currentDownloadTask;
  }
}
