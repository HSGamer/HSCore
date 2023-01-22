package me.hsgamer.hscore.web;

import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

/**
 * Methods on web
 */
public final class WebUtils {

  private WebUtils() {

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

  /**
   * Create a new connection
   *
   * @param address            the address / URL
   * @param connectionConsumer the consumer to set the connection
   *
   * @return the connection
   *
   * @throws IOException If the URL is invalid or can't be connected
   */
  @NotNull
  public static URLConnection createConnection(@NotNull String address, Consumer<URLConnection> connectionConsumer) throws IOException {
    URLConnection connection = createConnection(address);
    connectionConsumer.accept(connection);
    return new URL(address).openConnection();
  }

  /**
   * Create a new HTTP connection
   *
   * @param address the address / URL
   *
   * @return the connection
   *
   * @throws IOException If the URL is invalid or can't be connected
   */
  @NotNull
  public static HttpURLConnection createHttpConnection(@NotNull String address) throws IOException {
    URLConnection connection = createConnection(address);
    if (connection instanceof HttpURLConnection) {
      return (HttpURLConnection) connection;
    }
    connection.setConnectTimeout(5);
    throw new IOException("The URL is not a HTTP URL");
  }

  /**
   * Create a new HTTP connection
   *
   * @param address            the address / URL
   * @param connectionConsumer the consumer to set the connection
   *
   * @return the connection
   *
   * @throws IOException If the URL is invalid or can't be connected
   */
  @NotNull
  public static HttpURLConnection createHttpConnection(@NotNull String address, Consumer<HttpURLConnection> connectionConsumer) throws IOException {
    HttpURLConnection connection = createHttpConnection(address);
    connectionConsumer.accept(connection);
    return connection;
  }

  /**
   * Create a new HTTPS connection
   *
   * @param address the address / URL
   *
   * @return the connection
   *
   * @throws IOException If the URL is invalid or can't be connected
   */
  @NotNull
  public static HttpsURLConnection createHttpsConnection(@NotNull String address) throws IOException {
    URLConnection connection = createConnection(address);
    if (connection instanceof HttpsURLConnection) {
      return (HttpsURLConnection) connection;
    }
    connection.setConnectTimeout(5);
    throw new IOException("The URL is not a HTTPS URL");
  }

  /**
   * Create a new HTTPS connection
   *
   * @param address            the address / URL
   * @param connectionConsumer the consumer to set the connection
   *
   * @return the connection
   *
   * @throws IOException If the URL is invalid or can't be connected
   */
  @NotNull
  public static HttpsURLConnection createHttpsConnection(@NotNull String address, Consumer<HttpsURLConnection> connectionConsumer) throws IOException {
    HttpsURLConnection connection = createHttpsConnection(address);
    connectionConsumer.accept(connection);
    return connection;
  }
}
