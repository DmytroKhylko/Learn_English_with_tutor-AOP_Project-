package controllers;

import play.mvc.*;

import java.util.Optional;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    public Result home(Http.Request request) {
      Optional<String> logIn = request.session().get("login");
      if(logIn.isEmpty()) {
        return ok(views.html.home.render(request));
      }
      Optional<String> status = request.session().get("status");
      if(status.equals("teacher"))
        return redirect(routes.HomeController.teacher());
      return redirect(routes.HomeController.student());
    }

    public Result signUp(Http.Request request){
      return ok(views.html.signup.render(request));
    }

    public Result student(Http.Request request){
      return request
        .session()
        .get("login")
        .map(logIn -> ok(views.html.student_home_temporary.render(logIn, request)))
        .orElseGet(() -> redirect(routes.HomeController.home()));
    }

    public Result teacher(Http.Request request){
      return request
        .session()
        .get("login")
        .map(logIn -> ok(views.html.teacher_home_temporary.render(logIn, request)))
        .orElseGet(() -> redirect(routes.HomeController.home()));
    }
}
