package models;

import play.db.*;
import javax.inject.*;
import java.sql.*;

@Singleton
public class AuthorizationDBConnection {

    private Database db;

    @Inject
    public AuthorizationDBConnection(Database db) {
    this.db = db;
    }

  public void addUser(String login, String password, String status) {
    try {
      Connection conn = db.getConnection();
      Statement stmt = conn.createStatement();

      String sql = String.format("INSERT INTO users (login, password, status) VALUES ('%s', '%s', '%s')", login, password, status);

      stmt.execute(sql);

      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    }
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
}
