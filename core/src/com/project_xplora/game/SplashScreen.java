package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The SplashScreen class is Project Xplorer's introduction screen.
 * <p>
 * Time taken to complete: 20 minutes.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * SpriteBatch <b> backgroundBatch </b> - Creates a SpriteBatch for the class.
 * <p>
 * Texture <b> backgroundTexture </b> - Store Textures for the class.
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */

public class SplashScreen extends GameObjectController {
	private SpriteBatch backgroundBatch;
	private Texture backgroundTexture;

	/**
	 * Class Constructor: Initializes fields.
	 * 
	 * @param settings
	 *            - Stores current settings
	 */
	public SplashScreen(Settings settings) {
		super(settings);
		backgroundBatch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("SplashScreen.png"));
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
	}

	/**
	 * Updates the libGDX game client screen. This method is called very
	 * frequently in order to update the screen. It also manages what is to be
	 * drawn for the splashScreen.
	 */
	public void update() {
		backgroundBatch.begin();
		backgroundBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundBatch.end();
	}

	@Override
	public void camSetup() {

	}

	@Override
	/** Disposes of all resources. */
	public void disposeAll() {
		backgroundBatch.dispose();
		backgroundTexture.dispose();
	}
}
