package com.project_xplora.game.educational.minigames;

import java.util.ArrayList;
import java.util.List;
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

public class EducationalQuiz extends GameObjectController {

	private SpriteBatch quizBackground;
	private Skin quizSkin;
	private Table quizTable;
	private Stage quizStage;
	private Texture quizScreen, lineSeparator;
	private BitmapFont quizFont;
	private TextButton option1, option2, option3;
	private List<Boolean> questionsAsked;
	private List<Texture> quizGraphics;
	private List<String> questions, choices, answers;
	private String userChosenAnswer;
	private int questionNumber;
	private boolean generateQuestion;
	private float screenWidth;
	private float screenHeight;
	private float relativeLocation;

	//Constructor
	public EducationalQuiz(Settings settings) {
		super(settings);
		quizBackground = new SpriteBatch();
		quizSkin = new Skin(Gdx.files.internal("uiskin.json"));
		quizTable = new Table();
		quizStage = new Stage();
		quizScreen = new Texture(Gdx.files.internal("ChestUnlockScreen.jpg"));
		lineSeparator = new Texture(Gdx.files.internal("LineSeparator.png"));
		quizFont = new BitmapFont(Gdx.files.internal("quizFont.fnt"));
		userChosenAnswer = "";
		generateQuestion = true;
		questionsAsked = new ArrayList<Boolean>();
		quizGraphics = new ArrayList<Texture>();
		questions = new ArrayList<String>();
		choices = new ArrayList<String>();
		answers = new ArrayList<String>();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		relativeLocation = screenWidth / screenHeight;

		option1 = new TextButton("A", quizSkin);
		option2 = new TextButton("B", quizSkin);
		option3 = new TextButton("C", quizSkin);
		option1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "a";
				generateQuestion = true;
			}
		});
		option2.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "b";
				generateQuestion = true;
			}
		});

		option3.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "c";
				generateQuestion = true;
			}
		});
		quizTable.add(option1);
		quizTable.row();
		quizTable.add(option2);
		quizTable.row();
		quizTable.add(option3);
		quizTable.setX(1300f);
		quizTable.setY(500f);
		quizTable.pack();
		quizTable.center();
		quizStage.addActor(quizTable);
	}

	//Modifier Method
	public void addElements(Texture graphic, String question, String choice1, String choice2, String choice3,
			String answer) {
		quizGraphics.add(graphic);
		questions.add(question);
		choices.add(choice1);
		choices.add(choice2);
		choices.add(choice3);
		answers.add(answer);
		questionsAsked.add(false);
	}

	//Checks if all the questions have been answered
	private boolean isCycleFinished() {
		for (Boolean sweeper : questionsAsked) {
			if (!sweeper)
				return false;
		}
		return true;
	}

	//Resets the question tracker, allowing all questions to be asked again
	private void resetCycle() {
		for (int i = 0; i < questionsAsked.size(); i++) {
			questionsAsked.set(i, false);
		}
	}

	//Generates a question to be asked
	private int generateQuestionNumber() {
		int questionNumber;
		if (isCycleFinished()) {
			resetCycle();
		}
		while (true) {
			questionNumber = (int) (10 * Math.random());
			if (!questionsAsked.get(questionNumber))
				break;
		}
		questionsAsked.set(questionNumber, true);
		return questionNumber;
	}

	//@Override - Overrides camera setup method
	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(quizStage);
	}

	//Draws onto the screen every update
	public void regularUpdates(int questionNumber) {
		quizBackground.draw(quizScreen, 0, 0, screenWidth, screenHeight);
		quizBackground.draw(lineSeparator, 850, 250);
		quizFont.draw(quizBackground, "Question: " + questions.get(questionNumber), 90, 750);
		quizFont.draw(quizBackground, "a) " + choices.get(3 * questionNumber), 1100, 550);
		quizFont.draw(quizBackground, "b) " + choices.get(1 + (3 * questionNumber)), 1100, 475);
		quizFont.draw(quizBackground, "c) " + choices.get(2 + (3 * questionNumber)), 1100, 400);
	}

	//Updates screen
	public void update() {
		if (generateQuestion) {
			questionNumber = generateQuestionNumber();
			generateQuestion = false;
		}
		quizBackground.begin();
		regularUpdates(questionNumber);
		quizBackground.end();
		quizStage.draw();
	}

	//Dispose of all resources
	public void disposeAll() {
		quizBackground.dispose();
		quizStage.dispose();
		quizSkin.dispose();
		quizScreen.dispose();
		quizFont.dispose();
		quizTable = null;
	}
}