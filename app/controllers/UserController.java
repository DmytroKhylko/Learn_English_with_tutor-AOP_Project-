package controllers;

import play.data.FormFactory;
import play.mvc.*;
import play.filters.csrf.*;
import play.data.Form;
import javax.inject.Inject;
import javax.inject.Singleton;

import models.AuthorizationDBConnection;

@Singleton
public class UserController extends Controller{

  private final Form<UserData> userForm;
  private final Form<UserLogInData> userLogInForm;

  private final AuthorizationDBConnection db;

  @Inject
  public UserController(FormFactory formFactory, AuthorizationDBConnection db) {
    this.userForm = formFactory.form(UserData.class);
    this.userLogInForm = formFactory.form(UserLogInData.class);
    this.db = db;
  }

  @RequireCSRFCheck
  public Result signin(Http.Request request){
    final Form<UserData> boundForm = userForm.bindFromRequest(request);

    if (boundForm.hasErrors()) {
      return badRequest(views.html.signin.render(request));
    } else {

      UserData data = boundForm.get();

      if(!db.alreadySignedIn(data.getLogin())) {
        db.addUser(data.getLogin(), data.getPassword(), data.getStatus());
        if (data.getStatus().equals("student")) {
          return redirect(routes.HomeController.student(data.getLogin()));
        }
        else return redirect(routes.HomeController.teacher(data.getLogin()));
      }
      else return badRequest(views.html.signin.render(request)).flashing("danger", "LogIn already used!");
    }
  }
  @RequireCSRFCheck
  public Result login(Http.Request request) {
    final Form<UserLogInData> boundForm = userLogInForm.bindFromRequest(request);

    if (boundForm.hasErrors()) {
      return badRequest(views.html.home.render(request));
    } else {

      UserLogInData logInData = boundForm.get();

      if(db.correctLogInData(logInData.getLogin(), logInData.getPassword())) {
        if (db.getStatus(logInData.getLogin()).equals("teacher")) {
          return redirect(routes.HomeController.teacher(logInData.getLogin()));
        } else return redirect(routes.HomeController.student(logInData.getLogin()));
      }else return badRequest(views.html.home.render(request));
    }
  }
}
