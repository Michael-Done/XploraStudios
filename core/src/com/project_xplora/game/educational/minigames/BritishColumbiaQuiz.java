package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.project_xplora.game.GameObjectController;
import com.project_xplora.game.ProjectXploraGame;
import com.project_xplora.game.Settings;
import com.project_xplora.game.settings_menu_classes.KeyHandlerStage;

public class BritishColumbiaQuiz extends GameObjectController {
	private SpriteBatch quizBackground;
	private Skin quizSkin;
	private Stage quizStage;
	private Texture quizScreen;
	private BitmapFont quizFont;
	//private TextButton option1, option2, option3;
	//private Texture[] questionGraphics, questionText, questionOptions, answers;
	//private Texture quizScreen;
	//private int questionIndex;
	//private boolean[] questionsAsked;

	public BritishColumbiaQuiz(Settings currentSettings) {
		super(currentSettings);
		Gdx.input.setCursorCatched(false);
		quizBackground = new SpriteBatch ();
		quizStage = new Stage();
		Gdx.input.setInputProcessor(quizStage);
		quizSkin = new Skin(Gdx.files.internal("uiskin.json"));
		quizScreen = new Texture(Gdx.files.internal("ChestUnlockScreen.jpg"));
		quizFont = quizSkin.getFont("font-label");
		//questionsAsked = new boolean [9];
		//quizScreen = new Texture(Gdx.files.internal("ChestUnlockScreen.jpg"));
		//questionGraphics = new Texture [9];
		//questionText = new Texture [9];
		//questionOptions = new Texture [29];
		//answers = new Texture [9];
	}
	
	public void drawScreen () {
		quizBackground.draw(quizScreen, 0, 0);
		quizFont.setColor(Color.BLACK);
		quizFont.draw(quizBackground, "Testing", 200, 200);
	}

	public void update() {
		quizBackground.begin();
		drawScreen();
		quizBackground.end();
		quizStage.draw();
	}

	public void disposeAll() {
		quizStage.dispose();
		quizSkin.dispose();
		quizScreen.dispose();
		quizFont.dispose();
	}
}
