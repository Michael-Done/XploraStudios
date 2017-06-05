package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CreditsScene extends GameObjectController {
	private SpriteBatch backgroundBatch;
	private Texture backgroundTexture;

	public CreditsScene(Settings settings) {
		super(settings);
		backgroundBatch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("CreditsScene.jpg"));
	}

	public void update() {
		backgroundBatch.begin();
		backgroundBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundBatch.end();
	}
	

	@Override
	public void disposeAll() {
		backgroundBatch.dispose();
		backgroundTexture.dispose();
	}

}
