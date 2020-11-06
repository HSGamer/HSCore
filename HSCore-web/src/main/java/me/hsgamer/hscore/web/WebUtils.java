package me.hsgamer.hscore.web;

import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Methods on web
 */
public class WebUtils {

  private WebUtils() {

  }

  /**
   * Get JSON from the URL
   *
   * @param address   the URL
   * @param userAgent the user agent
   *
   * @return the JSON object
   *
   * @throws IOException    If there is an error when opening the connection
   * @throws ParseException If it fails to parse the JSON object
   */
  @NotNull
  public static Object getJSONFromURL(@NotNull String address, @NotNull UserAgent userAgent) throws IOException, ParseException {
    return new JSONParser().parse(new InputStreamReader(openConnection(address, userAgent).getInputStream()));
  }

  /**
   * Open a connection to the URL
   *
   * @param address   the address / URL
   * @param userAgent the user agent
   *
   * @return the connection
   *
   * @throws IOException If the URL is invalid or can't be connected
   */
  @NotNull
  public static URLConnection openConnection(@NotNull String address, @NotNull UserAgent userAgent) throws IOException {
    URLConnection openConnection = createConnection(address);
    openConnection.addRequestProperty("User-Agent", userAgent.getAgent());
    openConnection.connect();
    return openConnection;
  }

  /**
   * Create a new connection
   *
   * @param address the address / URL
   *
   * @return the connection
   *
   * @throws IOException If the URL is invalid or can't be connected
   */
  @NotNull
  public static URLConnection createConnection(@NotNull String address) throws IOException {
    return new URL(address).openConnection();
  }
}
