package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.Client;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The interface for SQL client
 *
 * @param <T> the original
 */
public interface SqlClient<T> extends Client<T> {

  /**
   * Get the connection
   *
   * @return the connection
   *
   * @throws SQLException if there is an SQL error
   */
  Connection getConnection() throws SQLException;
}
