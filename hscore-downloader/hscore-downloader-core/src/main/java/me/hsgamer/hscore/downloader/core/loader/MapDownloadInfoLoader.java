package me.hsgamer.hscore.downloader.core.loader;

import me.hsgamer.hscore.downloader.core.Downloader;
import me.hsgamer.hscore.downloader.core.object.DownloadInfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple {@link DownloadInfoLoader} that loads the download info from a map.
 * The format of the map:
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
public abstract class MapDownloadInfoLoader implements DownloadInfoLoader {
  private static final Logger logger = Logger.getLogger(MapDownloadInfoLoader.class.getSimpleName());

  private DownloadInfo getDownloadInfo(Downloader downloader, String name, Map<String, Object> map) {
    if (!map.containsKey("file-name")) {
      throw new IllegalArgumentException("The download info doesn't have a file name");
    }
    String fileName = String.valueOf(map.get("file-name"));

    if (!map.containsKey("version")) {
      throw new IllegalArgumentException("The download info doesn't have a version");
    }
    String version = String.valueOf(map.get("version"));

    if (!map.containsKey("direct-link")) {
      throw new IllegalArgumentException("The download info doesn't have a direct link");
    }
    String directLink = String.valueOf(map.get("direct-link"));

    return new DownloadInfo(name, fileName, version, directLink, map, downloader);
  }

  /**
   * Load the map for the {@link DownloadInfo} parser
   *
   * @param downloader the downloader
   *
   * @return the map
   */
  protected abstract CompletableFuture<Map<String, Map<String, Object>>> loadMap(Downloader downloader);

  @Override
  public CompletableFuture<Map<String, DownloadInfo>> load(Downloader downloader) {
    return loadMap(downloader).thenApply(stringMapMap -> {
      if (stringMapMap == null) {
        return Collections.emptyMap();
      }
      Map<String, DownloadInfo> map = new LinkedHashMap<>();

      stringMapMap.forEach((name, value) -> {
        try {
          DownloadInfo downloadInfo = getDownloadInfo(downloader, name, value);
          map.put(name, downloadInfo);
        } catch (Exception e) {
          logger.log(Level.WARNING, e, () -> "Something wrong when parsing the info of " + name);
        }
      });

      return map;
    });
  }
}
