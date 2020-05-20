package models;

import org.mindrot.jbcrypt.BCrypt;
import play.db.*;
import play.mvc.Http;

import javax.inject.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

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

  public LinkedList<String> getLinkedUsers(String user){
    LinkedList<String> linkedUsers = new LinkedList<>();
    try {
      Connection conn = db.getConnection();
      Statement stmt = conn.createStatement();

      String sql = String.format("SELECT linkedUser FROM relations WHERE user = '%s'", user);

      ResultSet result = stmt.executeQuery(sql);
      while(result.next()){
        linkedUsers.add(result.getString("linkedUser"));
      }
      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    }
    return linkedUsers;
  }
  public boolean linkUser(String user, String linkUser){
    if(alreadySignedIn(linkUser)){
      try {
        Connection conn = db.getConnection();
        Statement stmt = conn.createStatement();

        String sql = String.format("INSERT INTO relations (user, linkedUser) VALUES ('%s', '%s')",user, linkUser);

        stmt.execute(sql);

        sql = String.format("INSERT INTO relations (user, linkedUser) VALUES ('%s', '%s')",linkUser, user);
        stmt.execute(sql);

        conn.close();
        return true;
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return false;
  }

  public ArrayList<String> searchInDB(Http.Request request, String searchString, String status){
      ArrayList<String> searchResult = new ArrayList<>();
      if(searchString.equals("")) {
        searchResult.add("Input some data to search for");
        return searchResult;
      }
    String user = request.session().get("login").orElse("");
    try {
      Connection conn = db.getConnection();
      Statement stmt = conn.createStatement();

      String sql = String.format("SELECT login FROM users  WHERE status = '%s' AND login LIKE '%s'", status, "%"+searchString+"%");

      ResultSet result = stmt.executeQuery(sql);
      while(result.next()){
        if(!alreadyLinked(user, result.getString("login"))) {
          searchResult.add(result.getString("login"));
        }
      }
      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    }
    return searchResult;
  }

  public boolean alreadyLinked(String user, String linkedUser){
    try {
      Connection conn = db.getConnection();
      Statement stmt = conn.createStatement();

      String sql = String.format("SELECT linkedUser FROM relations  WHERE user = '%s'", user);

      ResultSet result = stmt.executeQuery(sql);
      while(result.next()){
        String linkedUserDB = result.getString("linkedUser");
        if(linkedUserDB.equals(linkedUser))
          return true;
      }
      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    }
      return false;
  }
}
