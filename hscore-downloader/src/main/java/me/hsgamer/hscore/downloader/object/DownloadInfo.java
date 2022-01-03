package me.hsgamer.hscore.downloader.object;

import me.hsgamer.hscore.downloader.Downloader;
import me.hsgamer.hscore.downloader.exception.DownloadingException;
import me.hsgamer.hscore.downloader.exception.RequiredInfoKeyException;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * The download information
 */
public final class DownloadInfo {
  private final JSONObject jsonObject;
  private final String name;
  private final String fileName;
  private final String version;
  private final String directLink;
  private final Downloader downloader;

  private boolean isDownloading = false;

  /**
   * Create a new download info
   *
   * @param name       the name
   * @param jsonObject the JSON object
   * @param downloader the downloader it belongs to
   */
  public DownloadInfo(String name, JSONObject jsonObject, Downloader downloader) {
    this.jsonObject = jsonObject;
    this.name = name;
    this.downloader = downloader;

    if (!jsonObject.containsKey("file-name")) {
      throw new RequiredInfoKeyException("The download info '" + name + "' doesn't have a file name");
    }
    this.fileName = String.valueOf(jsonObject.get("file-name"));

    if (!jsonObject.containsKey("version")) {
      throw new RequiredInfoKeyException("The download info '" + name + "' doesn't have a version");
    }
    this.version = String.valueOf(jsonObject.get("version"));

    if (!jsonObject.containsKey("direct-link")) {
      throw new RequiredInfoKeyException("The download info '" + name + "' doesn't have a direct link");
    }
    this.directLink = String.valueOf(jsonObject.get("direct-link"));
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
   * Get the JSON object
   *
   * @return the object
   */
  public JSONObject getJsonObject() {
    return jsonObject;
  }

  /**
   * Download the file
   *
   * @throws IOException          when an I/O error occurred
   * @throws DownloadingException if the addon is being downloaded
   */
  public void download() throws IOException {
    if (isDownloading) {
      throw new DownloadingException();
    }

    isDownloading = true;
    try (ReadableByteChannel readableByteChannel = Channels
      .newChannel(UserAgent.FIREFOX.assignToConnection(WebUtils.createConnection(directLink)).getInputStream());
         FileOutputStream fileOutputStream = new FileOutputStream(
           new File(downloader.getFolder(), fileName))) {
      fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
      isDownloading = false;
    } catch (IOException e) {
      isDownloading = false;
      throw e;
    }
  }
}
