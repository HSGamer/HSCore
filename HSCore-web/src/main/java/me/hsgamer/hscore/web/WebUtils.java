package me.hsgamer.hscore.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Methods on web
 */
public class WebUtils {

  private WebUtils() {

  }

  /**
   * Get JSON from the URL
   *
   * @param address the URL
   * @return the JSON object
   * @throws IOException    If there is an error when opening the connection
   * @throws ParseException If it fails to parse the JSON object
   */
  public static Object getJSONFromURL(String address) throws IOException, ParseException {
    BufferedReader rd = new BufferedReader(
        new InputStreamReader(openConnection(address).getInputStream()));
    return new JSONParser().parse(rd);
  }

  /**
   * Open a connection to the URL
   *
   * @param address the address / URL
   * @return the connection
   * @throws IOException If the URL is invalid or can't be connected
   */
  public static URLConnection openConnection(String address) throws IOException {
    URLConnection openConnection = createConnection(address);
    openConnection.addRequestProperty("User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
    openConnection.connect();
    return openConnection;
  }

  /**
   * Create a new connection
   *
   * @param address the address / URL
   * @return the connection
   * @throws IOException If the URL is invalid or can't be connected
   */
  public static URLConnection createConnection(String address) throws IOException {
    return new URL(address).openConnection();
  }
}
