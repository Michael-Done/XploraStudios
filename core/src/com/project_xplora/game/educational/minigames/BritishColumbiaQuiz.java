package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.project_xplora.game.GameObjectController;
import com.project_xplora.game.Settings;
import com.project_xplora.game.settings_menu_classes.KeyHandlerStage;

public class BritishColumbiaQuiz extends GameObjectController{

	private Stage quizStage;
	private Skin quizSkin;
	private Table quizTable; 
	private TextButton option1, option2, option3;
	
	
	//private Texture[] questionGraphics, questionText, questionOptions, answers;
	//private Texture quizScreen;
	//private int questionIndex;
	//private boolean[] questionsAsked;

	public BritishColumbiaQuiz(Settings currentSettings) {
		super (currentSettings);
		
		quizStage = new KeyHandlerStage ();
		
		quizSkin = new Skin(Gdx.files.internal("uiskin.json"));
		quizTable = new Table (quizSkin);
		loadModelInstances();
		
		//questionsAsked = new boolean [9];
		//quizScreen = new Texture(Gdx.files.internal("ChestUnlockScreen.jpg"));
		//questionGraphics = new Texture [9];
		//questionText = new Texture [9];
		//questionOptions = new Texture [29];
		//answers = new Texture [9];
	}
	
	public void update() {
		quizStage.draw();
	}

	public void loadModelInstances () {
		quizTable.clear();
		quizSkin.getFont("font-label").getData().setScale(Gdx.graphics.getWidth()/6400.0f, Gdx.graphics.getHeight()/3600.0f);
		quizSkin.getFont("font-button").getData().setScale(Gdx.graphics.getWidth()/6400.0f, Gdx.graphics.getHeight()/3600.0f);
		
		Label header1 = new Label("KEYBINDINGS", quizSkin);
		header1.setFontScale(1.1f);
		quizTable.setDebug(true);
		quizTable.add(header1);
		quizTable.add(new Label("hello", quizSkin));
		quizTable.row();
		quizTable.setX(Gdx.graphics.getWidth() / 2 - 500);
		quizTable.setY(Gdx.graphics.getHeight() / 2 - 500);
		quizTable.pack();
		quizTable.center();
		quizStage.addActor(quizTable);
	}
	
	public void setInputProccessor() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(quizStage);
	}
	
	public void disposeAll() {
		quizStage.dispose();
		quizSkin.dispose();
		quizTable = null;
	}
}
