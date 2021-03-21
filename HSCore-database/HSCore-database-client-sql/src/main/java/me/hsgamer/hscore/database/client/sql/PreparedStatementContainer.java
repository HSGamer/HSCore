package me.hsgamer.hscore.database.client.sql;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * The prepared statement container
 */
public class PreparedStatementContainer implements AutoCloseable {
  private final Connection connection;
  private final PreparedStatement statement;

  private PreparedStatementContainer(Connection connection, PreparedStatement statement) {
    this.connection = connection;
    this.statement = statement;
  }

  /**
   * Create a new statement container
   *
   * @param connection        the connection
   * @param preparedStatement the prepared statement
   *
   * @return the container
   */
  public static PreparedStatementContainer of(Connection connection, PreparedStatement preparedStatement) {
    return new PreparedStatementContainer(connection, preparedStatement);
  }

  /**
   * Create a new statement container
   *
   * @param connection the connection
   * @param statement  the statement
   * @param values     the values for the designated parameters
   *
   * @return the container
   *
   * @throws SQLException if there is an SQL error
   */
  public static PreparedStatementContainer of(Connection connection, @Language("SQL") String statement, Object... values) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(statement);
    for (int i = 0; i < values.length; i++) {
      preparedStatement.setObject(i + 1, values[i]);
    }
    return of(connection, preparedStatement);
  }

  /**
   * Query from the connection
   *
   * @return the result set
   *
   * @throws SQLException if there is an SQL error
   */
  public ResultSet query() throws SQLException {
    return this.statement.executeQuery();
  }

  /**
   * Update the database
   *
   * @return the row count or 0 for nothing
   *
   * @throws SQLException if there is an SQL error
   */
  public int update() throws SQLException {
    return this.statement.executeUpdate();
  }

  /**
   * Query from the connection but ignores the exception
   *
   * @return the result set
   */
  public Optional<ResultSet> querySafe() {
    try {
      return Optional.of(this.query());
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  /**
   * Update the database but ignores the exception
   *
   * @return the row count or 0 for nothing
   */
  public int updateSafe() {
    try {
      return this.update();
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  @Override
  public void close() throws SQLException {
    connection.close();
    statement.close();
  }

  /**
   * Get the statement
   *
   * @return the statement
   */
  public PreparedStatement getStatement() {
    return statement;
  }

  /**
   * Get the connection
   *
   * @return the connection
   */
  public Connection getConnection() {
    return connection;
  }
}
