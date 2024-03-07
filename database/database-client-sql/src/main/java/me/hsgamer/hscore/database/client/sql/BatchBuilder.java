package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.provider.LoggerProvider;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * The batch builder for continuous updating
 */
public class BatchBuilder implements AutoCloseable {
  private static final Logger LOGGER = LoggerProvider.getLogger(BatchBuilder.class);
  private final Connection connection;
  private final List<StatementBuilder> statementBuilderList = new LinkedList<>();

  /**
   * Create a new batch builder
   *
   * @param connection the connection
   */
  public BatchBuilder(Connection connection) {
    this.connection = connection;
  }

  /**
   * Add a statement as batch
   *
   * @param statement the statement
   * @param values    the values for the designated parameters
   */
  public void addBatch(@Language("SQL") String statement, Object... values) {
    this.statementBuilderList.add(StatementBuilder.create(connection).setStatement(statement).addValues(values));
  }

  /**
   * Execute the batches
   *
   * @throws SQLException if there is an SQL error
   */
  public void execute() throws SQLException {
    for (StatementBuilder statementBuilder : this.statementBuilderList) {
      statementBuilder.update();
    }
  }

  /**
   * Execute the batches but ignores exceptions
   */
  public void executeSafe() {
    try {
      this.execute();
    } catch (Exception e) {
      LOGGER.log(LogLevel.WARN, e);
    }
  }

  /**
   * Clear the batches
   */
  public void clear() {
    this.statementBuilderList.clear();
  }

  @Override
  public void close() {
    this.clear();
  }
}
