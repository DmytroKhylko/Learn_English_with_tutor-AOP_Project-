package controllers;

import play.data.FormFactory;
import play.mvc.*;
import play.filters.csrf.*;
import play.data.Form;
import javax.inject.Inject;
import javax.inject.Singleton;

import models.AuthorizationDBConnection;

import static models.AuthorizationDBConnection.createPassword;

@Singleton
public class UserController extends Controller{

  private final Form<UserSignUpData> userForm;
  private final Form<UserLogInData> userLogInForm;

  private final AuthorizationDBConnection db;

  @Inject
  public UserController(FormFactory formFactory, AuthorizationDBConnection db) {
    this.userForm = formFactory.form(UserSignUpData.class);
    this.userLogInForm = formFactory.form(UserLogInData.class);
    this.db = db;
  }

  @RequireCSRFCheck
  public Result signUp(Http.Request request){
    final Form<UserSignUpData> boundForm = userForm.bindFromRequest(request);

    if (boundForm.hasErrors()) {
      return badRequest(views.html.signup.render(request));
    } else {

      UserSignUpData data = boundForm.get();

      if(!db.alreadySignedIn(data.getLogin())) {
        db.addUser(data.getLogin(), createPassword(data.getPassword()), data.getStatus());
        if (data.getStatus().equals("student")) {
          return redirect(routes.HomeController.student()).addingToSession(request, "login", data.getLogin()).addingToSession(request, "status", db.getStatus(data.getLogin()));
        }
        return redirect(routes.HomeController.teacher()).addingToSession(request, "login", data.getLogin()).addingToSession(request, "status", db.getStatus(data.getLogin()));
      }
      return badRequest(views.html.signup.render(request)).flashing("danger", "LogIn already used!");
    }
  }
  @RequireCSRFCheck
  public Result logIn(Http.Request request) {
    final Form<UserLogInData> boundForm = userLogInForm.bindFromRequest(request);

    if (boundForm.hasErrors()) {
      return badRequest(views.html.home.render(request));
    }

    UserLogInData logInData = boundForm.get();

    if(db.correctLogInData(logInData.getLogin(), logInData.getPassword())) {
      if (db.getStatus(logInData.getLogin()).equals("teacher")) {
        return redirect(routes.HomeController.teacher()).addingToSession(request, "login", logInData.getLogin()).addingToSession(request, "status", db.getStatus(logInData.getLogin()));
      }
      return redirect(routes.HomeController.student()).addingToSession(request, "login", logInData.getLogin()).addingToSession(request, "status", db.getStatus(logInData.getLogin()));
    }
    return badRequest(views.html.home.render(request));

  }
  public Result logOut(Http.Request request){
    return redirect(routes.HomeController.home()).removingFromSession(request, "login");
  }
}
