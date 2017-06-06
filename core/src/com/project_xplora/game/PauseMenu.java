/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The pause menu class. This class is used in all 3 of the levels in order
 * to pause the game.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * Stage <b> stage </b> - The stage
 * <p>
 * Table <b> table </b> - The table
 * <p>
 * Skin <b> skin </b> - The GUI skin
 * <p>
 * InputProcessor <b> previousInput </b> - The previous input processor
 * <p>
 * boolean <b> previousCursorCatched </b> - the previous cursor catched state
 * <p>
 * public int <b> pause </b> - the pause key
 * <p>
 * Time taken to complete: 1.5 hours
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class PauseMenu {
	Stage stage;
	Table table;
	Skin skin;
	InputProcessor previousInput;
	boolean previousCursorCatched;
	public int PAUSE = Keys.ESCAPE;

	/** the subclass of Stage used to properly handle input */
	public class PauseStage extends Stage {
		public boolean paused;

		@Override
		public boolean keyDown(int keyCode) {
			super.keyDown(keyCode);
			if (keyCode == PAUSE) {
				paused = false;
				return true;
			}
			return false;
		}

	}

	/**
	 * Class constructor. constructs a basic pause menu.
	 */
	public PauseMenu() {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new PauseStage();
		table = new Table();
		TextButton quitGame = new TextButton("Quit Game", skin);
		quitGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		table.clear();
		table.add(new Label("Game Paused", skin));
		table.row();
		table.add(quitGame);
		table.row();

		table.pack();
		table.setX(Gdx.graphics.getWidth() / 2 - table.getWidth() / 2);
		table.setY(Gdx.graphics.getHeight() / 2 - table.getHeight() / 2);
		stage.addActor(table);
	}
	/**
	 * updates the menu
	 */
	public void update() {
		stage.draw();
	}
	/**
	 * sets the instance variables to previous settings
	 */
	public void setPrevious() {
		previousInput = Gdx.input.getInputProcessor();
		previousCursorCatched = Gdx.input.isCursorCatched();
	}
	/**
	 * restores previous settings
	 */
	public void restorePrevious() {
		Gdx.input.setInputProcessor(previousInput);
		Gdx.input.setCursorCatched(previousCursorCatched);
	}
}
