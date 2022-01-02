package me.hsgamer.hscore.database.client.sql;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The batch builder for continuous updating
 */
public class BatchBuilder implements AutoCloseable {
  private final Connection connection;
  private final List<PreparedStatementContainer> statementContainerList = new LinkedList<>();

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
   *
   * @throws SQLException if there is an SQL error
   */
  public void addBatch(@Language("SQL") String statement, Object... values) throws SQLException {
    this.statementContainerList.add(PreparedStatementContainer.of(connection, statement, values));
  }

  /**
   * Add a statement as batch but ignores exceptions
   *
   * @param statement the statement
   * @param values    the values for the designated parameters
   */
  public void addBatchSafe(@Language("SQL") String statement, Object... values) {
    try {
      this.addBatch(statement, values);
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is a error when adding", e);
    }
  }

  /**
   * Execute the batches
   *
   * @throws SQLException if there is an SQL error
   */
  public void execute() throws SQLException {
    for (PreparedStatementContainer statementContainer : this.statementContainerList) {
      statementContainer.update();
    }
  }

  /**
   * Execute the batches but ignores exceptions
   */
  public void executeSafe() {
    try {
      this.execute();
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is a error when executing", e);
    }
  }

  /**
   * Clear the batches
   *
   * @throws SQLException if there is an SQL error
   */
  public void clear() throws SQLException {
    for (PreparedStatementContainer statementContainer : this.statementContainerList) {
      statementContainer.close();
    }
    this.statementContainerList.clear();
  }

  /**
   * Clear the batches but ignores exceptions
   */
  public void clearSafe() {
    try {
      this.clear();
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is a error when clearing", e);
    }
  }

  @Override
  public void close() throws Exception {
    this.clear();
  }
}
