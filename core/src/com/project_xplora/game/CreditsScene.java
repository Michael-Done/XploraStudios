package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The CreditsScene class is Project Xplorer's final Credits manager.
 * <p>
 * Time taken to complete: 20 minutes.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * SpriteBatch <b> backgroundBatch </b> - Creates a SpriteBatch for the class.
 * <p>
 * Texture <b> backgroundTexture, closingTexture </b> - Stores textures for the
 * class.
 * <p>
 * int <b> counter </b> - Used to sychronize closing animations.
 * <p>
 * float <b> screenWidth, screenHeight </b> - Store the height and width of the
 * current screens.
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */

public class CreditsScene extends GameObjectController {
	private SpriteBatch backgroundBatch;
	private Texture backgroundTexture, closingTexture;
	private int counter;
	private float screenWidth, screenHeight;

	/**
	 * Class Constructor: Initializes object with settings passed through
	 * parameter as well as initializes all fields.
	 * 
	 * @param settings
	 *            - Stores the current settings.
	 */
	public CreditsScene(Settings settings) {
		super(settings);
		backgroundBatch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("CreditsScene.jpg"));
		closingTexture = new Texture(Gdx.files.internal("CreditsCloser.jpg"));
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		counter = 0;
	}

	/**
	 * Updates the libGDX game client screen. This method is called very
	 * frequently in order to update the screen. It also manages what is to be
	 * drawn for the credits screen.
	 */
	public void update() {
		backgroundBatch.begin();
		backgroundBatch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
		if (counter >= 240) {
			backgroundBatch.draw(closingTexture, (0 - screenWidth) + (-480 + (counter * 2)), 0, screenWidth,
					screenHeight);
			backgroundBatch.draw(closingTexture, screenWidth - (-480 + (counter * 2)), 0, screenWidth, screenHeight);
		}
		backgroundBatch.end();
		counter++;
		if (counter == 720) {
			Gdx.app.exit();
		}
	}

	@Override
	public void camSetup() {

	}

	/** Disposes of all assets */
	@Override
	public void disposeAll() {
		backgroundBatch.dispose();
		backgroundTexture.dispose();
	}

}
