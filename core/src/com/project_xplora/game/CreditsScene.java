package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CreditsScene extends GameObjectController {
	private SpriteBatch backgroundBatch;
	private Texture backgroundTexture, closingTexture;
	private int counter;
	private float screenWidth, screenHeight;

	public CreditsScene(Settings settings) {
		super(settings);
		backgroundBatch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("CreditsScene.jpg"));
		closingTexture = new Texture(Gdx.files.internal("CreditsCloser.jpg"));
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		counter = 0;
	}

	public void update() {
		backgroundBatch.begin();
		backgroundBatch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
		if (counter >= 240) {
			backgroundBatch.draw(closingTexture, (0 - screenWidth) + (-480 + (counter * 2)), 0, screenWidth, screenHeight);
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

	@Override
	public void disposeAll() {
		backgroundBatch.dispose();
		backgroundTexture.dispose();
	}

}
