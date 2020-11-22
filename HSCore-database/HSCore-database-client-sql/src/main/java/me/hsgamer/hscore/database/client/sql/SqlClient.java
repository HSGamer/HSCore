package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.Client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

  /**
   * Query from the connection
   *
   * @param command the query command
   *
   * @return the result set
   *
   * @throws SQLException if there is an SQL error
   */
  default ResultSet query(String command) throws SQLException {
    try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
      return statement.executeQuery(command);
    }
  }

  /**
   * Execute the commands
   *
   * @param command the command
   *
   * @throws SQLException if there is an SQL error
   */
  default void execute(String... command) throws SQLException {
    try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
      for (String cmd : command) {
        statement.executeUpdate(cmd);
      }
    }
  }
}
