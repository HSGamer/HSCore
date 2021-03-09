package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.Client;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
   * @param query  the query command
   * @param values the values for the designated parameters
   *
   * @return the result set
   *
   * @throws SQLException if there is an SQL error
   */
  default ResultSet query(@Language("SQL") String query, Object... values) throws SQLException {
    try (Connection connection = this.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      for (int i = 0; i < values.length; i++) {
        preparedStatement.setObject(i + 1, values[i]);
      }
      return preparedStatement.executeQuery();
    }
  }

  /**
   * Query from the connection but ignores the exception
   *
   * @param query  the query command
   * @param values the values for the designated parameters
   *
   * @return the result set
   */
  default Optional<ResultSet> querySafe(@Language("SQL") String query, Object... values) {
    try {
      return Optional.of(this.query(query, values));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Update the database
   *
   * @param command the update command
   * @param values  the values for the designated parameters
   *
   * @return the row count or 0 for nothing
   *
   * @throws SQLException if there is an SQL error
   */
  default int update(@Language("SQL") String command, Object... values) throws SQLException {
    try (Connection connection = this.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(command)) {
      for (int i = 0; i < values.length; i++) {
        preparedStatement.setObject(i + 1, values[i]);
      }
      return preparedStatement.executeUpdate();
    }
  }

  /**
   * Update the database but ignores the exception
   *
   * @param command the update command
   * @param values  the values for the designated parameters
   *
   * @return the row count or 0 for nothing
   */
  default int updateSafe(@Language("SQL") String command, Object... values) {
    try {
      return update(command, values);
    } catch (Exception e) {
      return 0;
    }
  }
}
