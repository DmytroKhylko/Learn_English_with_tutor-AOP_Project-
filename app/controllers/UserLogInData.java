package controllers;

import play.data.validation.Constraints;

public class UserLogInData {
  @Constraints.Required
  public String login;

  @Constraints.Required
  public String password;

  public UserLogInData() {
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
