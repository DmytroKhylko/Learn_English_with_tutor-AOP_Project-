package models;

import org.mindrot.jbcrypt.BCrypt;
import play.db.*;
import javax.inject.*;
import java.sql.*;

@Singleton
public class AuthorizationDBConnection {

    private final Database db;

    @Inject
    public AuthorizationDBConnection(Database db) {
    this.db = db;
    }

  public void addUser(String login, String password, String status) {
    db.withConnection(connection -> {
      Statement stmt = connection.createStatement();
      String sql = String.format("INSERT INTO users (login, password, status) VALUES ('%s', '%s', '%s')", login, password, status);
      stmt.execute(sql);
    });
  }

  public boolean alreadySignedIn(String login){
    try {
      Connection conn = db.getConnection();
      Statement stmt = conn.createStatement();

      String sql = String.format("SELECT * FROM users WHERE login = '%s'", login);

      ResultSet result = stmt.executeQuery(sql);
      while(result.next()){
        String loginFromDB = result.getString("login");
        if(loginFromDB.equals(login)){
          conn.close();
          return true;
        }
      }
      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    }
      return false;
  }

  public boolean correctLogInData(String login, String password){
    try {
      Connection conn = db.getConnection();
      Statement stmt = conn.createStatement();

      String sql = String.format("SELECT login, password FROM users WHERE login = '%s'", login);

      ResultSet result = stmt.executeQuery(sql);
      while(result.next()){
        String logInFromDB = result.getString("login");
        String passwordFromDB = result.getString("password");
        if(logInFromDB.equals(login) && checkPassword(password, passwordFromDB)){
          return true;
        }
      }
      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    }
      return false;
  }

  public String getStatus(String login){
    try {
      Connection conn = db.getConnection();
      Statement stmt = conn.createStatement();

      String sql = String.format("SELECT status FROM users WHERE login = '%s'", login);

      ResultSet result = stmt.executeQuery(sql);
      while(result.next()){
        String statusFromDB = result.getString("status");
        if(statusFromDB.equals("teacher")){
          return "teacher";
        }else if(statusFromDB.equals("student")){
          return "student";
        }
      }
      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    }
    return null;
  }

  public static String createPassword(String clearString){
    return BCrypt.hashpw(clearString, BCrypt.gensalt());
  }

  public static boolean checkPassword(String password, String encryptedPassword) {
    if (password == null) {
      return false;
    }
    if (encryptedPassword == null) {
      return false;
    }
    return BCrypt.checkpw(password, encryptedPassword);
  }
}
