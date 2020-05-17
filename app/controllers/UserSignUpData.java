package controllers;

import play.data.validation.Constraints;

public class UserSignUpData extends UserLogInData{

  @Constraints.Required
  private String status;

  public UserSignUpData(){}

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
