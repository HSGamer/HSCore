package me.hsgamer.hscore.downloader.json;

import me.hsgamer.hscore.downloader.core.Downloader;
import me.hsgamer.hscore.downloader.core.loader.DownloadInfoLoader;
import me.hsgamer.hscore.downloader.core.loader.MapDownloadInfoLoader;
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
import java.util.concurrent.CompletionException;

/**
 * A simple {@link DownloadInfoLoader} that loads the download info from a JSON file.
 */
public class JsonDownloadInfoLoader extends MapDownloadInfoLoader {
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
  protected CompletableFuture<Map<String, Map<String, Object>>> loadMap(Downloader downloader) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return new InputStreamReader(WebUtils.createConnection(dbUrl, userAgent::assignToConnection).getInputStream());
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
      Map<String, Map<String, Object>> map = new HashMap<>();
      // noinspection unchecked
      ((JSONObject) jsonObject).forEach((key, raw) -> {
        if (!(raw instanceof JSONObject)) {
          return;
        }
        String name = String.valueOf(key);
        Map<String, Object> valueMap = new HashMap<>();
        // noinspection unchecked
        ((JSONObject) raw).forEach((key1, raw1) -> valueMap.put(String.valueOf(key1), raw1));
        map.put(name, valueMap);
      });
      return map;
    });
  }
}
