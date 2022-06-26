package me.hsgamer.hscore.downloader.loader;

import me.hsgamer.hscore.downloader.Downloader;
import me.hsgamer.hscore.downloader.object.DownloadInfo;
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
import java.util.concurrent.CompletableFuture;

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

  @Override
  public CompletableFuture<Map<String, DownloadInfo>> load(Downloader downloader) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return new InputStreamReader(userAgent.assignToConnection(WebUtils.createConnection(dbUrl)).getInputStream());
      } catch (IOException e) {
        throw new IllegalStateException("Something wrong when getting the info", e);
      }
    }).thenApplyAsync(inputStreamReader -> {
      try {
        return new JSONParser().parse(inputStreamReader);
      } catch (IOException | ParseException e) {
        throw new IllegalArgumentException("Something wrong when parsing the info", e);
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
        map.put(name, new DownloadInfo(name, value, downloader));
      });
      return map;
    });
  }
}
