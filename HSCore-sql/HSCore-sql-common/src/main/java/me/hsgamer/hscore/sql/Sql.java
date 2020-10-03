package me.hsgamer.hscore.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Sql<T> {

  T getOriginal();

  Connection getConnection() throws SQLException;

  default ResultSet query(String command) throws SQLException {
    Connection connection = getConnection();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(command);
    connection.close();
    statement.close();
    return resultSet;
  }

  default void execute(String... command) throws SQLException {
    Connection connection = getConnection();
    Statement statement = connection.createStatement();
    for (String cmd : command) {
      statement.executeUpdate(cmd);
    }
    connection.close();
    statement.close();
  }
}
