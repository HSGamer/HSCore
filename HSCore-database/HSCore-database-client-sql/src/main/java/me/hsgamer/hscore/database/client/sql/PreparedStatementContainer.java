package me.hsgamer.hscore.database.client.sql;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The prepared statement container
 */
public class PreparedStatementContainer implements AutoCloseable {
  private final PreparedStatement statement;

  private PreparedStatementContainer(PreparedStatement statement) {
    this.statement = statement;
  }

  /**
   * Create a new statement container
   *
   * @param preparedStatement the prepared statement
   *
   * @return the container
   */
  public static PreparedStatementContainer of(PreparedStatement preparedStatement) {
    return new PreparedStatementContainer(preparedStatement);
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
    return of(preparedStatement);
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

  @Override
  public void close() throws SQLException {
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
  public Connection getConnection() throws SQLException {
    return statement.getConnection();
  }
}
