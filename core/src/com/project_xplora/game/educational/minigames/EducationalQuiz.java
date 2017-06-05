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
	private Texture quizScreen, lineSeparator, checkMark, crossMark, samplePicture;
	private BitmapFont quizFont;
	private TextButton option1, option2, option3, continueButton;
	private List<Boolean> questionsAsked;
	private List<String> questions, choices, answers;
	private String userChosenAnswer;
	private int questionNumber;
	private boolean generateQuestion, isCorrect, exitMinigame;
	private ClickListener listener1, listener2, listener3;

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
		checkMark = new Texture(Gdx.files.internal("Check_mark.png"));
		crossMark = new Texture(Gdx.files.internal("Red-Cross-Mark-PNG.png"));
		quizFont = new BitmapFont(Gdx.files.internal("quizFont.fnt"));
		userChosenAnswer = "";
		generateQuestion = true;
		questionsAsked = new ArrayList<Boolean>();
		questions = new ArrayList<String>();
		choices = new ArrayList<String>();
		answers = new ArrayList<String>();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		relativeLocation = screenWidth / screenHeight;

		option1 = new TextButton("A", quizSkin);
		option2 = new TextButton("B", quizSkin);
		option3 = new TextButton("C", quizSkin);
		listener1 = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "a";
			}
		};
		listener2 = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "b";
			}
		};
		listener3 = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "c";
			}
		};

		continueButton = new TextButton("Continue", quizSkin);
		option1.addListener(listener1);
		option2.addListener(listener2);
		option3.addListener(listener3);
		continueButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				option1.addListener(listener1);
				option2.addListener(listener2);
				option3.addListener(listener3);
				if (isCorrect) {
					exitMinigame = true;
					generateQuestion = true;
				} else {
					generateQuestion = true;
				}
			}
		});
		quizTable.add(option1).width(80).padTop(30).padLeft(50);
		quizTable.row();
		quizTable.add(option2).width(80).padTop(30).padLeft(50);
		quizTable.row();
		quizTable.add(option3).width(80).padTop(30).padLeft(50);
		quizTable.row();
		quizTable.add(continueButton).padTop(155);
		quizTable.setX(1580f);
		quizTable.setY(65f);
		quizTable.pack();
		quizTable.center();
		quizStage.addActor(quizTable);
	}

	//Modifier Method
	public void addElements(String question, String choice1, String choice2, String choice3, String answer) {
		questions.add(question);
		choices.add(choice1);
		choices.add(choice2);
		choices.add(choice3);
		answers.add(answer);
		questionsAsked.add(false);
	}

	//Modifier Method #2
	public void addQuestionTexture(Texture passedTexture) {
		samplePicture = passedTexture;
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

	private int setMarkLocation() {
		if (userChosenAnswer.equals("a")) {
			return 0;
		} else if (userChosenAnswer.equals("b")) {
			return 1;
		} else {
			return 2;
		}
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
		quizBackground.draw(lineSeparator, 850, 150);
		quizBackground.draw(samplePicture, 140, 190, 800, 450);
		quizFont.draw(quizBackground, "Question: " + questions.get(questionNumber), 90, 750);
		quizFont.draw(quizBackground, "(Click on one of the multiple choice boxes)", 90, 695);
		quizFont.draw(quizBackground, "a) " + choices.get(3 * questionNumber), 1100, 585);
		quizFont.draw(quizBackground, "b) " + choices.get(1 + (3 * questionNumber)), 1100, 470);
		quizFont.draw(quizBackground, "c) " + choices.get(2 + (3 * questionNumber)), 1100, 355);
		if (!userChosenAnswer.equals("")) {
			option1.removeListener(listener1);
			option2.removeListener(listener2);
			option3.removeListener(listener3);
			int markLocation = setMarkLocation();
			if (userChosenAnswer.equals(answers.get(questionNumber))) {
				quizBackground.draw(checkMark, 1800, 545 - (markLocation * 115), 50, 50);
				quizFont.draw(quizBackground, "Correct!", 1100, 240);
				quizFont.draw(quizBackground, "Your chest is now unlocked! Press <Continue> to resume the game.", 140, 100);
				isCorrect = true;
			} else {
				quizBackground.draw(crossMark, 1800, 545 - (markLocation * 115), 50, 50);
				quizFont.draw(quizBackground, "Incorrect!", 1100, 260);
				quizFont.draw(quizBackground, "Correct Answer: " + answers.get(questionNumber).toUpperCase(), 1100, 220);
				quizFont.draw(quizBackground, "A time penalty of 30 seconds will be added.", 140, 125);
				quizFont.draw(quizBackground, "Press <Continue> to try answering another question.", 140, 85);
				isCorrect = false;
			}
		}
	}

	//Updates screen
	public void update() {
		if (generateQuestion) {
			questionNumber = generateQuestionNumber();
			generateQuestion = false;
			userChosenAnswer = "";
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
	public boolean isCorrect(){
		return exitMinigame;
	}
	public void resetExitMinigame(){
		exitMinigame = false;
	}
}