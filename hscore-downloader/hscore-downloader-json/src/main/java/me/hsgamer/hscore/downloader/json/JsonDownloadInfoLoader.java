package me.hsgamer.hscore.downloader.json;

import me.hsgamer.hscore.downloader.core.Downloader;
import me.hsgamer.hscore.downloader.core.loader.DownloadInfoLoader;
import me.hsgamer.hscore.downloader.core.object.DownloadInfo;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple {@link DownloadInfoLoader} that loads the download info from a JSON file.
 * The format of the json file:
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
public class JsonDownloadInfoLoader implements DownloadInfoLoader {
  private static final Logger logger = Logger.getLogger(JsonDownloadInfoLoader.class.getSimpleName());
  private final String dbUrl;
  private final UserAgent userAgent;

  /**
   * Create a new {@link JsonDownloadInfoLoader}
   *
   * @param dbUrl     the url of the json file
   * @param userAgent the user agent to use
   */
  public JsonDownloadInfoLoader(String dbUrl, UserAgent userAgent) {
    this.dbUrl = dbUrl;
    this.userAgent = userAgent;
  }

  /**
   * Create a new {@link JsonDownloadInfoLoader}
   *
   * @param dbUrl the url of the json file
   */
  public JsonDownloadInfoLoader(String dbUrl) {
    this(dbUrl, UserAgent.FIREFOX);
  }

  private DownloadInfo getDownloadInfo(Downloader downloader, String name, JSONObject jsonObject) {
    if (!jsonObject.containsKey("file-name")) {
      throw new IllegalArgumentException("The download info doesn't have a file name");
    }
    String fileName = String.valueOf(jsonObject.get("file-name"));

    if (!jsonObject.containsKey("version")) {
      throw new IllegalArgumentException("The download info doesn't have a version");
    }
    String version = String.valueOf(jsonObject.get("version"));

    if (!jsonObject.containsKey("direct-link")) {
      throw new IllegalArgumentException("The download info doesn't have a direct link");
    }
    String directLink = String.valueOf(jsonObject.get("direct-link"));

    Map<String, Object> data = new HashMap<>();
    // noinspection unchecked
    jsonObject.forEach((key, value) -> data.put(Objects.toString(key, ""), value));

    return new DownloadInfo(name, fileName, version, directLink, data, downloader);
  }

  @Override
  public CompletableFuture<Map<String, DownloadInfo>> load(Downloader downloader) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return new InputStreamReader(userAgent.assignToConnection(WebUtils.createConnection(dbUrl)).getInputStream());
      } catch (IOException e) {
        throw new CompletionException("Something wrong when getting the info", e);
      }
    }).thenApplyAsync(inputStreamReader -> {
      try {
        return new JSONParser().parse(inputStreamReader);
      } catch (IOException | ParseException e) {
        throw new CompletionException("Something wrong when parsing the info", e);
      }
    }).thenApplyAsync(jsonObject -> {
      if (!(jsonObject instanceof JSONObject)) {
        return Collections.emptyMap();
      }
      Map<String, DownloadInfo> map = new HashMap<>();
      // noinspection unchecked
      ((JSONObject) jsonObject).forEach((key, raw) -> {
        if (!(raw instanceof JSONObject)) {
          return;
        }
        JSONObject value = (JSONObject) raw;
        String name = String.valueOf(key);
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
