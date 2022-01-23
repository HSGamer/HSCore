package me.hsgamer.hscore.database.client.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The executor to apply the {@link PreparedStatement}
 *
 * @param <T> The type of the result
 */
@FunctionalInterface
public interface SqlExecutor<T> {
  /**
   * Apply the prepared statement to get the result
   *
   * @param preparedStatement The prepared statement
   *
   * @return The result
   *
   * @throws SQLException If an SQL error occurs
   */
  T apply(PreparedStatement preparedStatement) throws SQLException;
}
