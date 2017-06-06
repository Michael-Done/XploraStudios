package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The InstructionsScene class is Project Xplorer's instructions scene manager.
 *  <p>
 * Time taken to complete: 20 minutes.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * SpriteBatch <b> backgroundBatch </b> - Creates a SpriteBatch for the class.
 * <p>
 * Texture <b> backgroundTexture1, backgroundTexture2, backgroundTexture3,
 * tempTexture </b> - Store Textures for the class.
 * <p>
 * int <b> currentScreen </b> - Stores the value of the current screen.
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class InstructionsScene extends GameObjectController {
	private SpriteBatch backgroundBatch;
	private Texture backgroundTexture1, backgroundTexture2, backgroundTexture3, tempTexture;
	private int currentScreen;

	/**
	 * Class Constructor: Initializes object with settings passed through
	 * parameter as well as initializes all fields.
	 * 
	 * @param settings
	 *            - Stores the current settings.
	 */
	public InstructionsScene(Settings settings) {
		super(settings);
		backgroundBatch = new SpriteBatch();
		backgroundTexture1 = new Texture(Gdx.files.internal("Instructions1.png"));
		backgroundTexture2 = new Texture(Gdx.files.internal("Instructions2.png"));
		backgroundTexture3 = new Texture(Gdx.files.internal("Instructions3.png"));
		currentScreen = 1;
		tempTexture = backgroundTexture1;
	}

	/**
	 * This method is called from the main game class when the user chooses to
	 * move onto the next screen. To move onto the next screen, the screen value
	 * variable is incremented.
	 */
	public void incrementScreen() {
		if (currentScreen == 1) {
			currentScreen = 2;
		} else if (currentScreen == 2) {
			currentScreen = 3;
		} else if (currentScreen == 3) {
			currentScreen = 1;
		}
	}

	/**
	 * This method is called from the main game class when the user chooses to
	 * move onto the next screen. To move onto the next screen, the screen value
	 * variable is incremented. As a result, the current texture being displayed
	 * is affected too.
	 */
	public void changeCurrentTexture() {
		if (currentScreen == 1) {
			tempTexture = backgroundTexture1;
		} else if (currentScreen == 2) {
			tempTexture = backgroundTexture2;
		} else if (currentScreen == 3) {
			tempTexture = backgroundTexture3;
		}
	}

	/**
	 * Called when the user exits the instructions scene and resumes back to the
	 * menu.
	 */
	public void resetMenuScreen() {
		currentScreen = 1;
		changeCurrentTexture();
	}

	/**
	 * Updates the libGDX game client screen. This method is called very
	 * frequently in order to update the screen. This manages what is being
	 * drawn for the instructions screen.
	 */
	public void update() {
		backgroundBatch.begin();
		backgroundBatch.draw(tempTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundBatch.end();
	}

	@Override
	public void camSetup() {

	}

	/** Disposes of all assets. */
	@Override
	public void disposeAll() {
		backgroundBatch.dispose();
		backgroundTexture1.dispose();
		backgroundTexture2.dispose();
		backgroundTexture3.dispose();
	}
}
