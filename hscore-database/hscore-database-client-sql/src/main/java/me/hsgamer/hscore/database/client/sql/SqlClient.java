package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.Client;

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
   * Create a new statement builder for this client
   *
   * @return the statement builder
   *
   * @throws SQLException if there is an SQL error
   */
  default StatementBuilder createStatementBuilder() throws SQLException {
    return StatementBuilder.create(this.getConnection());
  }

  /**
   * Create a new statement builder for this client but ignores exceptions
   *
   * @return the statement builder
   */
  default Optional<StatementBuilder> createStatementBuilderSafe() {
    try {
      return Optional.of(this.createStatementBuilder());
    } catch (Exception e) {
      e.printStackTrace();
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
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
