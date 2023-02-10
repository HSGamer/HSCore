package me.hsgamer.hscore.downloader.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.hsgamer.hscore.downloader.core.Downloader;
import me.hsgamer.hscore.downloader.core.loader.DownloadInfoLoader;
import me.hsgamer.hscore.downloader.core.loader.MapDownloadInfoLoader;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;

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
        return JsonParser.parseReader(inputStreamReader);
      } catch (Exception e) {
        throw new CompletionException("Something wrong when parsing the info", e);
      }
    }).thenApplyAsync(element -> {
      if (!element.isJsonObject()) {
        return Collections.emptyMap();
      }
      JsonObject jsonObject = element.getAsJsonObject();
      Map<String, Map<String, Object>> map = new HashMap<>();
      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        if (!entry.getValue().isJsonObject()) {
          continue;
        }
        JsonObject object = entry.getValue().getAsJsonObject();
        Map<String, Object> infoMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> infoEntry : object.entrySet()) {
          infoMap.put(infoEntry.getKey(), infoEntry.getValue().getAsString());
        }
        map.put(entry.getKey(), infoMap);
      }
      return map;
    });
  }
}
