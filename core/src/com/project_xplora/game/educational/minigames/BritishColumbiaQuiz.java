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

public class BritishColumbiaQuiz extends GameObjectController {
	private SpriteBatch quizBackground;
	private Skin quizSkin;
	private Table quizTable;
	private Stage quizStage;
	private Texture quizScreen, lineSeparator;
	//private Texture[] questionGraphics;
	private BitmapFont quizFont;

	private String[] questions, choices, answers;
	private String userChosenAnswer;
	private boolean[] questionsAsked;
	private TextButton option1, option2, option3;

	public BritishColumbiaQuiz(Settings currentSettings) {
		super(currentSettings);
		quizBackground = new SpriteBatch(); //
		quizStage = new Stage(); // 
		quizTable = new Table (); //
		quizSkin = new Skin(Gdx.files.internal("uiskin.json"));
		quizScreen = new Texture(Gdx.files.internal("ChestUnlockScreen.jpg"));
		lineSeparator = new Texture(Gdx.files.internal("LineSeparator.png"));
		quizFont = new BitmapFont(Gdx.files.internal("quizFont.fnt"));
		option1 = new TextButton("A", quizSkin);
		option2 = new TextButton("B", quizSkin);
		option3 = new TextButton("C", quizSkin);

		questionsAsked = new boolean[10];
		questions = new String[10];
		questions[0] = "Which landform region is British Columbia situated in?";
		questions[1] = "What is the name of the ocean that is along the coast of British Columbia?";
		questions[2] = "What is the name of the world-renowned mountain range in British Columbia?";
		questions[3] = "Is British Columbia a province, territory or a state?";
		questions[4] = "What is the capital of British Columbia?";
		questions[5] = "In terms of size, British Columbia is the ____ biggest province in Canada";
		questions[6] = "Which of these mountains is found in British Columbia?";
		questions[7] = "Which of these rivers is found in British Columbia?";
		questions[8] = "Which province is beside British Columbia?";
		questions[9] = "What type of climate does British Columbia generally have?";

		choices = new String[30];
		choices[0] = "Western Cordillera Region";
		choices[1] = "Interior Plains";
		choices[2] = "Hudson Bay Lowlands";
		choices[3] = "Atlantic Ocean";
		choices[4] = "Pacific Ocean";
		choices[5] = "Arctic Ocean";
		choices[6] = "Himalayas";
		choices[7] = "Appalachian Mountains";
		choices[8] = "The Rocky Mountains";
		choices[9] = "Province";
		choices[10] = "Territory";
		choices[11] = "State";
		choices[12] = "Vancouver";
		choices[13] = "Victoria";
		choices[14] = "Kelowna";
		choices[15] = "1st";
		choices[16] = "2nd";
		choices[17] = "3rd";
		choices[18] = "Mount Logan";
		choices[19] = "Mount Saint Elias";
		choices[20] = "Whistler Monutain";
		choices[21] = "Fraser River";
		choices[22] = "St. Lawrence River";
		choices[23] = "Mackenzie River";
		choices[24] = "Yukon";
		choices[25] = "Ontario";
		choices[26] = "Alberta";
		choices[27] = "Maritime Climate";
		choices[28] = "Continental Climate";
		choices[29] = "Polar Climate";

		answers = new String[10];
		answers[0] = "a";
		answers[1] = "b";
		answers[2] = "c";
		answers[3] = "a";
		answers[4] = "b";
		answers[5] = "c";
		answers[6] = "c";
		answers[7] = "a";
		answers[8] = "c";
		answers[9] = "a";
	}

	private boolean isCycleFinished() {
		for (boolean b : questionsAsked) {
			if (!b)
				return false;
		}
		return true;
	}

	private void resetCycle() {
		for (int i = 0; i < questionsAsked.length; i++) {
			questionsAsked[i] = false;
		}
	}

	private int generateQuestionNumber() {
		int questionNumber;
		if (isCycleFinished()) {
			resetCycle();
		}
		while (true) {
			questionNumber = (int) (10 * Math.random());
			if (questionsAsked[questionNumber] == false)
				break;
		}
		questionsAsked[questionNumber] = true;
		return questionNumber;
	}

	@Override
	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(quizStage);
	}

	public void drawScreen() {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int questionNumber = generateQuestionNumber();

		//Screen Setup
		quizBackground.draw(quizScreen, 0, 0, screenWidth, screenHeight);
		quizBackground.draw(lineSeparator, 850, 250);

		//Question
		quizFont.draw(quizBackground, "Question: " + questions[questionNumber], 90, 750);

		//Choices
		quizFont.draw(quizBackground, "a) " + choices[3 * questionNumber], 1100, 550);
		quizFont.draw(quizBackground, "b) " + choices[1 + (3 * questionNumber)], 1100, 475);
		quizFont.draw(quizBackground, "c) " + choices[2 + (3 * questionNumber)], 1100, 400);

		//Add Listeners to Buttons 
		option1.addListener (new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "a";
			}
		});
		
		option2.addListener (new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "b";
			}
		});
		
		option3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "c";
			}
		});
		
		//Add Buttons to Stage
		quizTable.add(option1);
		quizTable.row();
		quizTable.add(option2);
		quizTable.row();
		quizTable.add(option3);
		quizTable.setX(1300f);
		quizTable.setY(500f);
		quizTable.pack();
		quizTable.center();
		quizStage.addActor (quizTable);
	}

	public void update() {
		quizBackground.begin();
		drawScreen();
		quizBackground.end();
		quizStage.draw();
	}

	public void disposeAll() {
		quizBackground.dispose();
		quizStage.dispose();
		quizSkin.dispose();
		quizScreen.dispose();
		quizFont.dispose();
		quizTable = null;
	}
}
