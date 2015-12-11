package br.edu.ufba.softvis.visminer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.edu.ufba.softvis.visminer.model.database.FileDB;

public class MySQLConnection {

  private static MySQLConnection mySQLConnection = null;

  private MySQLConnection(){

    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  public static MySQLConnection getInstance(){

    if(mySQLConnection == null)
      mySQLConnection = new MySQLConnection();

    return mySQLConnection;

  }

  private Connection getConnection(){

    try {
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/visminer?"
          + "user=root&password=1234");
      return conn;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;

  }

  public FileDB findFileById(int id){

    Connection conn = getConnection();
    Statement stmt;
    try {

      stmt = conn.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT * FROM visminer.file where id = "+id);

      FileDB fileDb = new FileDB();
      fileDb.setId(id);

      rs.next();
      fileDb.setPath(rs.getString("PATH"));
      fileDb.setUid(rs.getString("UID"));
      conn.close();
      
      return fileDb;

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
    
  }

}
