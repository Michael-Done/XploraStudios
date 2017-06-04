package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project_xplora.game.GameObjectController;
import com.project_xplora.game.Settings;

public class BritishColumbiaQuiz extends EducationalQuiz {
	private Texture placeHolder;

	public BritishColumbiaQuiz(Settings currentSettings) {
		super(currentSettings);
		placeHolder = new Texture (Gdx.files.internal(""));
		
		super.addElements(placeHolder, "Which landform region is British Columbia situated in?",
				"Western Cordillera Region", "Interior Plains", "Hudson Bay Lowlands", "a");
		super.addElements(placeHolder, "What is the name of the ocean that is along the coast of British Columbia?",
				"Atlantic Ocean", "Pacific Ocean", "Arctic Ocean", "b");
		super.addElements(placeHolder, "What is the name of the world-renowned mountain range in British Columbia?",
				"Himalayas", "Appalachian Mountains", "The Rocky Mountains", "c");
		super.addElements(placeHolder, "Is British Columbia a province, territory or a state?", "Province", "Territory",
				"State", "a");
		super.addElements(placeHolder, "What is the capital of British Columbia?", "Vancouver", "Victoria", "Kelowna",
				"b");
		super.addElements(placeHolder, "In terms of size, British Columbia is the ____ biggest province in Canada",
				"1st", "2nd", "3rd", "c");
		super.addElements(placeHolder, "Which of these mountains is found in British Columbia?", "Mount Logan",
				"Mount Saint Elias", "Whistler Monutain", "c");
		super.addElements(placeHolder, "Which of these rivers is found in British Columbia?", "Fraser River",
				"St. Lawrence River", "Mackenzie River", "a");
		super.addElements(placeHolder, "Which province is beside British Columbia?", "Yukon", "Ontario", "Alberta",
				"c");
		super.addElements(placeHolder, "What type of climate does British Columbia generally have?", "Maritime Climate",
				"Continental Climate", "Polar Climate", "a");

	}
}
