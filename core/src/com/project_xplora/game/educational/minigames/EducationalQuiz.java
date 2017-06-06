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
import com.project_xplora.game.ProjectXploraGame;
import com.project_xplora.game.Settings;

/**
 * The EducationalQuiz class creates the template for Project Xplorer's
 * educational mini game component.
 * <p>
 * Time taken to complete: 3 hours.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * SpriteBatch <b> quizBackground </b> - Creates a SpriteBatch for the class.
 * <p>
 * Skin <b> quizSkin </b> - Creates a Skin for the class.
 * <p>
 * Table <b> quizTable </b> - Creates a Table for the class.
 * <p>
 * Stage <b> quizStage </b> - Creates a Stage for the class.
 * <p>
 * Texture <b> quizScreen, checkMark, crossMark, samplePicture </b> - Store
 * Textures for the class.
 * <p>
 * BitmapFont <b> quizFont </b> - Stores and renders the BitmapFont for this
 * class.
 * <p>
 * TextButton <b> option1, option2, option3, continueButton </b> - Creates
 * TextButtons for the class.
 * <p>
 * List &lt;Boolean&gt <b> questionsAsked </b> - Keeps track of the questions
 * that have been asked.
 * <p>
 * List &lt;String&gt <b> questions, choices, answers </b> - Stores the
 * questions, choices and answers.
 * <p>
 * String <b> userChosenAnswer </b> - Stores the answer that the user has
 * chosen.
 * <p>
 * int <b> questionNumber </b> - Keeps track of the current question number.
 * <p>
 * boolean <b> generateQuestion, isCorrect, exitMinigame </b> - Logic Variables
 * <p>
 * ClickListener <b> listener1, listener2, listener3, listener3 </b> - Listener
 * interfaces for the buttons.
 * <p>
 * float <b> screenWidth, screenHeight </b> - Stores the screen dimensions.
 * <h2>Course Info:</h2> ICS4U0 with Krasteva, V.
 * <p>
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */

public class EducationalQuiz extends GameObjectController {

	//Fields
	private SpriteBatch quizBackground;
	private Skin quizSkin;
	private Table quizTable;
	private Stage quizStage;
	private Texture quizScreen, checkMark, crossMark, samplePicture;
	private BitmapFont quizFont;
	private TextButton option1, option2, option3, continueButton;
	private List<Boolean> questionsAsked;
	private List<String> questions, choices, answers;
	private String userChosenAnswer;
	private int questionNumber;
	private boolean generateQuestion, isCorrect, exitMinigame;
	private ClickListener listener1, listener2, listener3, listener4;
	private float screenWidth;
	private float screenHeight;

	/**
	 * EducationalQuiz Class Constructor. Initializes all assets, fields,
	 * buttons and listener interfaces. Additionally, creates the quiz table and
	 * adds it to the stage.
	 * 
	 * @param settings
	 *            - Stores the current settings.
	 */
	public EducationalQuiz(Settings settings) {
		super(settings);
		quizBackground = new SpriteBatch();
		quizSkin = new Skin(Gdx.files.internal("uiskin.json"));
		quizTable = new Table();
		quizStage = new Stage();
		quizScreen = new Texture(Gdx.files.internal("ChestUnlockScreen.jpg"));
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

		option1 = new TextButton("A", quizSkin);
		option2 = new TextButton("B", quizSkin);
		option3 = new TextButton("C", quizSkin);
		listener1 = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "a";
				continueButton.addListener(listener4);
			}
		};
		listener2 = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "b";
				continueButton.addListener(listener4);
			}
		};
		listener3 = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				userChosenAnswer = "c";
				continueButton.addListener(listener4);
			}
		};
		listener4 = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				option1.addListener(listener1);
				option2.addListener(listener2);
				option3.addListener(listener3);
				if (isCorrect) {
					exitMinigame = true;
					generateQuestion = true;
				} else {
					ProjectXploraGame.timer.add30();
					generateQuestion = true;
				}
				continueButton.removeListener(listener4);
			}
		};
		continueButton = new TextButton("Continue", quizSkin);
		option1.addListener(listener1);
		option2.addListener(listener2);
		option3.addListener(listener3);
		quizTable.add(option1).width(80).padTop(30).padLeft(50);
		quizTable.row();
		quizTable.add(option2).width(80).padTop(30).padLeft(50);
		quizTable.row();
		quizTable.add(option3).width(80).padTop(30).padLeft(50);
		quizTable.row();
		quizTable.add(continueButton).padTop(80);
		quizTable.setX(500f);
		quizTable.setY(140f);
		quizTable.pack();
		quizTable.center();
		quizStage.addActor(quizTable);
	}

	/**
	 * Modifier Method which allows the EducationalQuiz class to become an
	 * editable template. This method allows all subclasses to directly
	 * implement their question, choice and answer systems through the
	 * EducationalQuiz class that works as a template.
	 * 
	 * @param question
	 *            - Stores the question to be added.
	 * @param choice1
	 *            - Stores the first choice to be added.
	 * @param choice2
	 *            - Stores the second choice to be added.
	 * @param choice3
	 *            - Stores the third choice to be added.
	 * @param answer
	 *            - Stores the answer to be added.
	 */
	public void addElements(String question, String choice1, String choice2, String choice3, String answer) {
		questions.add(question);
		choices.add(choice1);
		choices.add(choice2);
		choices.add(choice3);
		answers.add(answer);
		questionsAsked.add(false);
	}

	/**
	 * Second modifier method that allows the subclasses to import their stock
	 * graphic/image for their minigame screen.
	 * 
	 * @param passedTexture
	 *            - Stores the texture to be placed on the quiz template.
	 */
	public void addQuestionTexture(Texture passedTexture) {
		samplePicture = passedTexture;
	}

	/**
	 * Checks if all the questions of the quiz have already been answered. This
	 * method does so by using a sequential search algorithm.
	 * 
	 * @return Returns false if there are still questions to be asked. Returns
	 *         true otherwise.
	 */
	private boolean isCycleFinished() {
		for (Boolean sweeper : questionsAsked) {
			if (!sweeper)
				return false;
		}
		return true;
	}

	/**
	 * Resets the question tracker which allows all questions to be asked again.
	 * The for loop is used to iterate through each element and set it to a
	 * value of false.
	 */
	private void resetCycle() {
		for (int i = 0; i < questionsAsked.size(); i++) {
			questionsAsked.set(i, false);
		}
	}

	/**
	 * Method used to generate the question number to be asked for the quiz.
	 * First, the method checks if all questions have been asked. If that is
	 * true, this method resets the question cycle. Next, it searches for a
	 * question number that has not been used in the current cycle. Once that
	 * has been found, this question number is now considered "used" and is then
	 * returned as a value.
	 * 
	 * @return Returns a random question number between 0 to 9 to be used by
	 *         another method.
	 */
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

	/**
	 * Method used for the check mark and cross mark location. Depending on
	 * which button, a, b or c, has been clicked, the markLocation will appear
	 * accordingly.
	 * 
	 * @return Returns a number that corresponds with the multiple choice button
	 *         that has been pressed. A = 0, B = 1, C = 2.
	 */
	private int setMarkLocation() {
		if (userChosenAnswer.equals("a")) {
			return 0;
		} else if (userChosenAnswer.equals("b")) {
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(quizStage);
	}

	/**
	 * Formats quiz output screen, manages quiz mechanics such as correct/wrong
	 * answers and manages listener interfaces.
	 * 
	 * @param questionNumber
	 *            - Current question to be displayed.
	 */
	public void regularUpdates(int questionNumber) {
		quizBackground.draw(quizScreen, 0, 0, screenWidth, screenHeight);
		quizBackground.draw(samplePicture, 750, 300, 430, 305);
		quizFont.getData().setScale(0.7f);
		quizFont.draw(quizBackground, "Question: " + questions.get(questionNumber), 68, 750);
		quizFont.draw(quizBackground, "(Click on one of the multiple choice boxes)", 68, 695);
		quizFont.draw(quizBackground, "a) " + choices.get(3 * questionNumber), 85, 585);
		quizFont.draw(quizBackground, "b) " + choices.get(1 + (3 * questionNumber)), 85, 470);
		quizFont.draw(quizBackground, "c) " + choices.get(2 + (3 * questionNumber)), 85, 355);
		if (!userChosenAnswer.equals("")) {
			option1.removeListener(listener1);
			option2.removeListener(listener2);
			option3.removeListener(listener3);
			int markLocation = setMarkLocation();
			if (userChosenAnswer.equals(answers.get(questionNumber))) {
				quizBackground.draw(checkMark, 540, 545 - (markLocation * 115), 50, 50);
				quizFont.draw(quizBackground, "Correct!", 85, 240);
				quizFont.draw(quizBackground, "Your chest is now unlocked! Press <Continue> to resume the game.", 85,
						100);
				isCorrect = true;
			} else {

				quizBackground.draw(crossMark, 540, 545 - (markLocation * 115), 50, 50);
				quizFont.draw(quizBackground, "Incorrect!", 85, 260);
				quizFont.draw(quizBackground, "Correct Answer: " + answers.get(questionNumber).toUpperCase(), 85, 220);
				quizFont.draw(quizBackground, "A time penalty of 30 seconds will be added.", 85, 125);
				quizFont.draw(quizBackground, "Press <Continue> to try answering another question.", 85, 85);
				isCorrect = false;
			}
		}
	}

	/**
	 * Updates the libGDX game client screen. This method is called very
	 * frequently in order to update the screen. It also manages what is to be
	 * drawn for the minigame quiz.
	 */
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

	/** Disposes of all resources. */
	public void disposeAll() {
		quizBackground.dispose();
		quizStage.dispose();
		quizSkin.dispose();
		quizScreen.dispose();
		quizFont.dispose();
		quizTable = null;
	}

	/**
	 * Determines whether to exit he minigame or not.
	 * 
	 * @returns Returns true if the user answered the question correctly. False
	 *          otherwise.
	 */
	public boolean isCorrect() {
		return exitMinigame;
	}

	/** Resets the exitMinigame state variable. */
	public void resetExitMinigame() {
		exitMinigame = false;
	}
}