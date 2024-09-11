package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.Client;
import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.provider.LoggerProvider;

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
    return new StatementBuilder(this.getConnection());
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
      LoggerProvider.getLogger(this.getClass()).log(LogLevel.WARN, e);
      return Optional.empty();
    }
  }
}
