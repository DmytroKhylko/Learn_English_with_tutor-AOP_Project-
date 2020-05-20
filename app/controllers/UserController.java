package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import play.filters.csrf.*;
import play.data.Form;
import javax.inject.Inject;
import javax.inject.Singleton;

import models.AuthorizationDBConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static models.AuthorizationDBConnection.createPassword;

@Singleton
public class UserController extends Controller{

  private final Form<UserSignUpData> userForm;
  private final Form<UserLogInData> userLogInForm;
  private final Form<LinkUserData> linkUserDataForm;

  private final AuthorizationDBConnection db;

  @Inject
  public UserController(FormFactory formFactory, AuthorizationDBConnection db) {
    this.userForm = formFactory.form(UserSignUpData.class);
    this.userLogInForm = formFactory.form(UserLogInData.class);
    this.linkUserDataForm = formFactory.form(LinkUserData.class);
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
      Map<String, String> sessionValues =  new HashMap<>();
      sessionValues.put("login", logInData.getLogin());
      sessionValues.put("status", db.getStatus(logInData.getLogin()));
      if (db.getStatus(logInData.getLogin()).equals("teacher")) {
        return redirect(routes.HomeController.teacher()).addingToSession(request, sessionValues);
      }
      return redirect(routes.HomeController.student()).addingToSession(request, sessionValues);
    }
    return badRequest(views.html.home.render(request));

  }
  public Result logOut(Http.Request request){
    return redirect(routes.HomeController.home()).removingFromSession(request, "login", "status");
  }

  @RequireCSRFCheck
  public Result linkUser(Http.Request request){
    final Form<LinkUserData> boundForm = linkUserDataForm.bindFromRequest(request);
    if (boundForm.hasErrors()) {
      return badRequest(views.html.home.render(request));
    }
    LinkUserData connectWithUser = boundForm.get();
    db.linkUser(request.session().get("login").orElse("Empty link"), connectWithUser.getLinkUser());

    Optional<String> status = request.session().get("status");
    Optional<String> teacher = Optional.of("teacher");
    if(status.equals(teacher))
      return redirect(routes.HomeController.teacher());
    return redirect(routes.HomeController.student());
  }

  public Result search(Http.Request request){
    String searchStatus;
    JsonNode json = request.body().asJson();
    Optional<String> status = request.session().get("status");
    Optional<String> teacher = Optional.of("teacher");
    if(status.equals(teacher)){
      searchStatus = "student";
    }else searchStatus = "teacher";
    return ok(Json.toJson(db.searchInDB(json.get("searchString").asText(), searchStatus)));
  }
}
