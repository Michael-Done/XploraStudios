package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen extends GameObjectController {
	private SpriteBatch backgroundBatch;
	private Texture backgroundTexture;

	public SplashScreen(Settings settings) {
		super(settings);
		backgroundBatch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("SplashScreen.png"));
	}

	public void update() {
		backgroundBatch.begin();
		backgroundBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundBatch.end();
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
