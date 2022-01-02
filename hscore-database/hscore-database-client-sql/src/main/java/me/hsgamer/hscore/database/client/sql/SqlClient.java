package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.Client;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
   * Prepare the statement but ignores exceptions
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
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is an error when preparing the statement", e);
      return Optional.empty();
    }
  }

  /**
   * Create a new batch builder for this client
   *
   * @return the batch builder
   *
   * @throws SQLException if there is an SQL error
   */
  default BatchBuilder createBatchBuilder() throws SQLException {
    return new BatchBuilder(this.getConnection());
  }

  /**
   * Create a new batch builder for this client but ignores exceptions
   *
   * @return the batch builder
   */
  default Optional<BatchBuilder> createBatchBuilderSafe() {
    try {
      return Optional.of(this.createBatchBuilder());
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is an error when creating the batch builder", e);
      return Optional.empty();
    }
  }
}
