package me.hsgamer.hscore.database.client.sql;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link PreparedStatement} builder
 */
public class StatementBuilder {
  private final Connection connection;
  private final AtomicReference<String> statement = new AtomicReference<>("");
  private final List<Object> values = new ArrayList<>();

  private StatementBuilder(Connection connection) {
    this.connection = connection;
  }

  /**
   * Create a new builder for the connection
   *
   * @param connection the connection
   *
   * @return the builder
   */
  public static StatementBuilder create(Connection connection) {
    return new StatementBuilder(connection);
  }

  /**
   * Add values to the statement
   *
   * @param values the values
   *
   * @return the builder for chaining
   */
  public StatementBuilder addValues(Object... values) {
    Collections.addAll(this.values, values);
    return this;
  }

  /**
   * Add values to the statement
   *
   * @param values the values
   *
   * @return the builder for chaining
   */
  public StatementBuilder addValues(List<Object> values) {
    this.values.addAll(values);
    return this;
  }

  /**
   * Execute the statement
   *
   * @param sqlExecutor the executor
   * @param <T>         the type of the result
   *
   * @return the result
   *
   * @throws SQLException if there is an SQL error
   */
  public <T> T execute(SqlExecutor<T> sqlExecutor) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(statement.get())) {
      for (int i = 0; i < values.size(); i++) {
        preparedStatement.setObject(i + 1, values.get(i));
      }
      return sqlExecutor.apply(preparedStatement);
    }
  }

  /**
   * Query from the connection
   *
   * @return the result set
   *
   * @throws SQLException if there is an SQL error
   */
  public ResultSet query() throws SQLException {
    return this.execute(PreparedStatement::executeQuery);
  }

  /**
   * Update the database
   *
   * @return the row count or 0 for nothing
   *
   * @throws SQLException if there is an SQL error
   */
  public int update() throws SQLException {
    return this.execute(PreparedStatement::executeUpdate);
  }

  /**
   * Query from the connection but ignores exceptions
   *
   * @return the result set
   */
  public Optional<ResultSet> querySafe() {
    try {
      return Optional.of(this.query());
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is a error when querying", e);
      return Optional.empty();
    }
  }

  /**
   * Update the database but ignores exceptions
   *
   * @return the row count or 0 for nothing
   */
  public int updateSafe() {
    try {
      return this.update();
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is a error when updating", e);
      return 0;
    }
  }

  /**
   * Get the connection
   *
   * @return the connection
   */
  public Connection getConnection() {
    return connection;
  }

  /**
   * Get the statement
   *
   * @return the statement
   */
  public String getStatement() {
    return statement.get();
  }

  /**
   * Set the statement to execute
   *
   * @param statement the statement
   *
   * @return the builder for chaining
   */
  public StatementBuilder setStatement(@Language("SQL") String statement) {
    this.statement.set(statement);
    return this;
  }

  /**
   * Get the values
   *
   * @return the values
   */
  public List<Object> getValues() {
    return Collections.unmodifiableList(values);
  }
}
