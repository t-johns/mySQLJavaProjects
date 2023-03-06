package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import projects.exception.DbException;

public class DbConnection { //default connection class
  private static String HOST = "localhost"; 
  private static String PASSWORD = "projects";
  private static int PORT = 3306;
  private static String SCHEMA = "projects";
  private static String USER = "projects";

  public static java.sql.Connection getConnection() { // sql Connection class
    String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
        HOST, PORT, SCHEMA, USER, PASSWORD); // variable to String uri

    try {
      Connection conn = DriverManager.getConnection(uri); // try connection with driver
      System.out.println("Connection success!");
      return conn;
    } catch (SQLException e) { // exception
      System.out.println("Connection error has occurred.");
      throw new DbException(e); // throw new exception
    }
  }
}
