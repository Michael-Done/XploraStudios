package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ArtifactScene extends GameObjectController {
	private SpriteBatch backgroundBatch;
	private Texture backgroundTexture1, backgroundTexture2, backgroundTexture3, tempTexture;
	private int currentScreen;

	public ArtifactScene(Settings settings) {
		super(settings);
		backgroundBatch = new SpriteBatch();
		backgroundTexture1 = new Texture(Gdx.files.internal("Artifact1.png"));
		backgroundTexture2 = new Texture(Gdx.files.internal("Artifact2.png"));
		backgroundTexture3 = new Texture(Gdx.files.internal("Artifact3.png"));
		currentScreen = 1;
		tempTexture = backgroundTexture1;
	}

	public void incrementScreen() {
		if (currentScreen == 1) {
			currentScreen = 2;
		} else if (currentScreen == 2) {
			currentScreen = 3;
		} else if (currentScreen == 3) {
			currentScreen = 1;
		}
	}

	public void changeCurrentTexture() {
		if (currentScreen == 1) {
			tempTexture = backgroundTexture1;
		} else if (currentScreen == 2) {
			tempTexture = backgroundTexture2;
		} else if (currentScreen == 3) {
			tempTexture = backgroundTexture3;
		}
	}
	
	public void resetMenuScreen () {
		currentScreen = 1;
		changeCurrentTexture ();
	}

	public void update() {
		backgroundBatch.begin();
		backgroundBatch.draw(tempTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundBatch.end();
	}

	@Override
	public void camSetup() {

	}

	@Override
	public void disposeAll() {
		backgroundBatch.dispose();
		backgroundTexture1.dispose();
		backgroundTexture2.dispose();
		backgroundTexture3.dispose();
	}
}
