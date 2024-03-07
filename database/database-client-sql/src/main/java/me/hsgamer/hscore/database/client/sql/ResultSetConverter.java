package me.hsgamer.hscore.database.client.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The interface to convert the {@link ResultSet} to the final object
 *
 * @param <T> the type of the object
 */
public interface ResultSetConverter<T> {
  /**
   * Convert the result set to the final object
   *
   * @param resultSet The result set
   *
   * @return The final object
   *
   * @throws SQLException If an SQL error occurs
   */
  T convert(ResultSet resultSet) throws SQLException;
}
