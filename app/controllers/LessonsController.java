package controllers;
import play.data.FormFactory;
import play.mvc.*;
import java.util.ArrayList;

import play.filters.csrf.*;
import play.data.Form;
import models.Lesson;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LessonsController extends Controller {
	private ArrayList<Lesson> lessons;

	private final Form<LessonData> form;

	@Inject
	public LessonsController(FormFactory formFactory) {
		this.form = formFactory.form(LessonData.class);

		this.lessons = new ArrayList<>();
		this.lessons.add(new Lesson("verbs practice"));
		this.lessons.add(new Lesson("grammar practice"));
	}

	@RequireCSRFCheck
	public Result createLesson(Http.Request request) {
		final Form<LessonData> boundForm = form.bindFromRequest(request);

		if (boundForm.hasErrors()) {
			return badRequest(views.html.lessons.render(lessons, form, request));
		} else {
			LessonData data = boundForm.get();
			lessons.add(new Lesson(data.getName()));
			return redirect(routes.LessonsController.index())
				.flashing("success", "Lesson added!");
		}
	}

	public Result index(Http.Request request) {
		return ok(views.html.lessons.render(lessons, form, request));
	}
}
