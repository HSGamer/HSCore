package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.Client;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
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
   * Prepare the statement
   *
   * @param statement the statement
   * @param values    the values for the designated parameters
   *
   * @return the prepared statement container
   *
   * @throws SQLException if there is an SQL error
   */
  default PreparedStatementContainer prepareStatement(@Language("SQL") String statement, Object... values) throws SQLException {
    return PreparedStatementContainer.of(this.getConnection(), statement, values);
  }

  /**
   * Prepare the statement but ignores exceptions (Print them as warnings)
   *
   * @param statement the statement
   * @param values    the values for the designated parameters
   *
   * @return the prepared statement container
   */
  default Optional<PreparedStatementContainer> prepareStatementSafe(@Language("SQL") String statement, Object... values) {
    try {
      return Optional.of(this.prepareStatement(statement, values));
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
