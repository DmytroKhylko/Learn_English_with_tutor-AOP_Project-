package controllers;

import play.data.validation.Constraints;

public class UserSignUpData extends UserLogInData{

  @Constraints.Required
  public String status;

  public UserSignUpData(){}

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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
