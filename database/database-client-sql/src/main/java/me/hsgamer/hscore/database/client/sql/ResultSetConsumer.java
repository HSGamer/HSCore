package me.hsgamer.hscore.database.client.sql;

import java.sql.ResultSet;

/**
 * The interface to consume the {@link ResultSet}
 */
public interface ResultSetConsumer {
  /**
   * Consume the result set
   *
   * @param resultSet The result set
   */
  void consume(ResultSet resultSet);
}
