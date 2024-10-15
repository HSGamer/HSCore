package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.provider.LoggerProvider;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@link PreparedStatement} builder
 */
public class StatementBuilder {
  private static final Logger LOGGER = LoggerProvider.getLogger(StatementBuilder.class);
  private final Connection connection;
  private @Language("SQL") String statement;
  private Object[] values;

  private StatementBuilder(Connection connection) {
    this.connection = connection;
  }

  /**
   * Create a new builder
   *
   * @param connection the connection
   *
   * @return the builder
   */
  public static StatementBuilder create(Connection connection) {
    return new StatementBuilder(connection);
  }

  /**
   * Set the statement
   *
   * @param statement the statement
   *
   * @return the builder for chaining
   */
  public StatementBuilder setStatement(@Language("SQL") String statement) {
    this.statement = statement;
    return this;
  }

  /**
   * Set the values
   *
   * @param values the values
   *
   * @return the builder for chaining
   */
  public StatementBuilder setValues(Object... values) {
    this.values = values;
    return this;
  }

  /**
   * Set the values
   *
   * @param values the values
   *
   * @return the builder for chaining
   */
  public StatementBuilder setValues(List<Object> values) {
    return setValues(values.toArray());
  }

  /**
   * Add values to the current values
   *
   * @param values the values
   *
   * @return the builder for chaining
   */
  public StatementBuilder addValues(Object... values) {
    if (this.values == null || this.values.length == 0) {
      this.values = values;
    } else {
      Object[] newValues = new Object[this.values.length + values.length];
      System.arraycopy(this.values, 0, newValues, 0, this.values.length);
      System.arraycopy(values, 0, newValues, this.values.length, values.length);
      this.values = newValues;
    }
    return this;
  }

  /**
   * Add values to the current values
   *
   * @param values the values
   *
   * @return the builder for chaining
   */
  public StatementBuilder addValues(List<Object> values) {
    return addValues(values.toArray());
  }

  /**
   * Execute the statement
   *
   * @param executor the executor
   * @param <T>      the result type
   *
   * @return the result
   *
   * @throws SQLException if a SQL error occurs
   */
  public <T> T execute(Executor<T> executor) throws SQLException {
    if (statement == null) {
      throw new IllegalStateException("Statement is not set");
    }
    try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          preparedStatement.setObject(i + 1, values[i]);
        }
      }
      return executor.apply(preparedStatement);
    }
  }

  /**
   * Query the statement
   *
   * @param converter the converter
   * @param <T>       the result type
   *
   * @return the result
   *
   * @throws SQLException if a SQL error occurs
   */
  public <T> T query(ResultSetConverter<T> converter) throws SQLException {
    return execute(preparedStatement -> {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return converter.apply(resultSet);
      }
    });
  }

  /**
   * Query the statement and return a list
   *
   * @param converter the converter
   * @param <T>       the result type
   *
   * @return the result
   *
   * @throws SQLException if a SQL error occurs
   */
  public <T> List<T> queryList(ResultSetConverter<T> converter) throws SQLException {
    return query(resultSet -> {
      List<T> list = new ArrayList<>();
      while (resultSet.next()) {
        list.add(converter.apply(resultSet));
      }
      return list;
    });
  }

  /**
   * Consume the statement
   *
   * @param consumer the consumer
   *
   * @throws SQLException if a SQL error occurs
   */
  public void consume(ResultSetConsumer consumer) throws SQLException {
    execute(preparedStatement -> {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        consumer.accept(resultSet);
      }
      return null;
    });
  }

  /**
   * Update the statement
   *
   * @return the result
   *
   * @throws SQLException if a SQL error occurs
   */
  public int update() throws SQLException {
    return execute(PreparedStatement::executeUpdate);
  }

  /**
   * Query the statement but ignore the exception
   *
   * @param converter the converter
   * @param <T>       the result type
   *
   * @return the result
   */
  public <T> Optional<T> querySafe(ResultSetConverter<T> converter) {
    try {
      return Optional.of(query(converter));
    } catch (SQLException e) {
      LOGGER.log(LogLevel.WARN, e);
      return Optional.empty();
    }
  }

  /**
   * Query the statement and return a list but ignore the exception
   *
   * @param converter the converter
   * @param <T>       the result type
   *
   * @return the result
   */
  public <T> Optional<List<T>> queryListSafe(ResultSetConverter<T> converter) {
    try {
      return Optional.of(queryList(converter));
    } catch (SQLException e) {
      LOGGER.log(LogLevel.WARN, e);
      return Optional.empty();
    }
  }

  /**
   * Consume the statement but ignore the exception
   *
   * @param consumer the consumer
   */
  public void consumeSafe(ResultSetConsumer consumer) {
    try {
      consume(consumer);
    } catch (SQLException e) {
      LOGGER.log(LogLevel.WARN, e);
    }
  }

  /**
   * Update the statement but ignore the exception
   *
   * @return the result
   */
  public int updateSafe() {
    try {
      return update();
    } catch (SQLException e) {
      LOGGER.log(LogLevel.WARN, e);
      return -1;
    }
  }

  /**
   * The executor to execute the statement
   *
   * @param <T> the result type
   */
  public interface Executor<T> {
    /**
     * Apply the statement and return the result
     *
     * @param statement the statement
     *
     * @return the result
     *
     * @throws SQLException if a SQL error occurs
     */
    T apply(PreparedStatement statement) throws SQLException;
  }

  /**
   * The converter to convert the {@link ResultSet} to the result
   *
   * @param <T> the result type
   */
  public interface ResultSetConverter<T> {
    /**
     * Convert the {@link ResultSet} to the result
     *
     * @param resultSet the {@link ResultSet}
     *
     * @return the result
     *
     * @throws SQLException if a SQL error occurs
     */
    T apply(ResultSet resultSet) throws SQLException;
  }

  /**
   * The consumer to consume the {@link ResultSet}
   */
  public interface ResultSetConsumer {
    /**
     * Consume the {@link ResultSet}
     *
     * @param resultSet the {@link ResultSet}
     *
     * @throws SQLException if a SQL error occurs
     */
    void accept(ResultSet resultSet) throws SQLException;
  }
}
