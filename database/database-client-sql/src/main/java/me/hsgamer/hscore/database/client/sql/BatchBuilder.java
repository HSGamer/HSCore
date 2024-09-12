package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.provider.LoggerProvider;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A builder for batch execution
 */
public class BatchBuilder {
  private static final Logger LOGGER = LoggerProvider.getLogger(BatchBuilder.class);
  private final Connection connection;
  private final @Language("SQL") String statement;
  private final List<Object[]> values = new ArrayList<>();

  private BatchBuilder(Connection connection, @Language("SQL") String statement) {
    this.connection = connection;
    this.statement = statement;
  }

  /**
   * Create a new builder
   *
   * @param connection the connection
   * @param statement  the statement
   *
   * @return the builder
   */
  public static BatchBuilder create(Connection connection, @Language("SQL") String statement) {
    return new BatchBuilder(connection, statement);
  }

  /**
   * Add values to the batch
   *
   * @param values the values
   *
   * @return this builder for chaining
   */
  public BatchBuilder addValues(Object... values) {
    this.values.add(values);
    return this;
  }

  /**
   * Add values to the batch
   *
   * @param values the values
   *
   * @return this builder for chaining
   */
  public BatchBuilder addValues(List<Object> values) {
    return addValues(values.toArray());
  }

  /**
   * Execute the batch
   *
   * @return the result of the batch
   *
   * @throws SQLException if a SQL error occurs
   */
  public int[] execute() throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
      for (Object[] value : values) {
        for (int i = 0; i < value.length; i++) {
          preparedStatement.setObject(i + 1, value[i]);
        }
        preparedStatement.addBatch();
      }
      return preparedStatement.executeBatch();
    }
  }

  /**
   * Execute the batch but ignore the exception
   *
   * @return the result of the batch
   */
  public int[] executeSafe() {
    try {
      return execute();
    } catch (SQLException e) {
      LOGGER.log(LogLevel.WARN, e);
      return new int[0];
    }
  }
}
