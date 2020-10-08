package me.hsgamer.hscore.database.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The interface for SQL connections
 *
 * @param <T> the original
 */
public interface Sql<T> {

  /**
   * Get the original
   *
   * @return the original
   */
  T getOriginal();

  /**
   * Get the connection
   *
   * @return the connection
   * @throws SQLException if there is an SQL error
   */
  Connection getConnection() throws SQLException;

  /**
   * Query from the connection
   *
   * @param command the query command
   * @return the result set
   * @throws SQLException if there is an SQL error
   */
  default ResultSet query(String command) throws SQLException {
    Connection connection = getConnection();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(command);
    connection.close();
    statement.close();
    return resultSet;
  }

  /**
   * Execute the commands
   *
   * @param command the command
   * @throws SQLException if there is an SQL error
   */
  default void execute(String... command) throws SQLException {
    Connection connection = getConnection();
    Statement statement = connection.createStatement();
    for (String cmd : command) {
      statement.executeUpdate(cmd);
    }
    connection.close();
    statement.close();
  }
}
