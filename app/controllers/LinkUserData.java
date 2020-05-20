package controllers;

import play.data.validation.Constraints;
public class LinkUserData {

  @Constraints.Required
  protected String linkUser;

  public LinkUserData() {}

  public String getLinkUser() {
    return linkUser;
  }

  public void setLinkUser(String linkUser) {
    this.linkUser = linkUser;
  }
}
