package controllers;

import play.data.validation.Constraints;

public class LessonData {
	@Constraints.Required
	public String name;

	public LessonData() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
