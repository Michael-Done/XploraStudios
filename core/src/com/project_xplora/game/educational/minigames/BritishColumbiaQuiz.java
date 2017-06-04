package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.project_xplora.game.Settings;

public class BritishColumbiaQuiz extends EducationalQuiz {

	public BritishColumbiaQuiz(Settings currentSettings) {
		super(currentSettings);
		super.addElements("Which landform region is British Columbia situated in?", "Western Cordillera Region",
				"Interior Plains", "Hudson Bay Lowlands", "a");
		super.addElements("What is the name of the ocean that is along the coast of British Columbia?",
				"Atlantic Ocean", "Pacific Ocean", "Arctic Ocean", "b");
		super.addElements("What is the name of the world-renowned mountain range in British Columbia?", "Himalayas",
				"Appalachian Mountains", "The Rocky Mountains", "c");
		super.addElements("Is British Columbia a province, territory or a state?", "Province", "Territory", "State",
				"a");
		super.addElements("What is the capital of British Columbia?", "Vancouver", "Victoria", "Kelowna", "b");
		super.addElements("In terms of size, British Columbia is the ____ biggest province in Canada", "1st", "2nd",
				"3rd", "c");
		super.addElements("Which of these mountains is found in British Columbia?", "Mount Logan", "Mount Saint Elias",
				"Whistler Mountain", "c");
		super.addElements("Which of these rivers is found in British Columbia?", "Fraser River", "St. Lawrence River",
				"Mackenzie River", "a");
		super.addElements("Which province is beside British Columbia?", "Yukon", "Ontario", "Alberta", "c");
		super.addElements("What type of climate does British Columbia generally have?", "Maritime Climate",
				"Continental Climate", "Polar Climate", "a");
		super.addQuestionTexture(new Texture (Gdx.files.internal("MinigameLevel1Screen.jpg")));
	}
}
