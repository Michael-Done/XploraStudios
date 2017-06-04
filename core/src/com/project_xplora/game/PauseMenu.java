/**
 * 
 */
package com.project_xplora.game;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project_xplora.game.settings_menu_classes.KeyField;

/**
 * @author Michael
 *
 */
public class PauseMenu {
	Stage stage;
	Table table;
	Skin skin;
	InputProcessor previousInput;
	boolean previousCursorCatched;
	public int PAUSE = Keys.ESCAPE;
	public boolean backToMenu = false;
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
	 * 
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
		
//		TextButton mainMenu = new TextButton("Main Menu", skin);
//		mainMenu.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				backToMenu = true;
//				((PauseStage)(stage)).paused = false;
//			}
//		});
		
		table.clear();
		table.add(new Label("Game Paused", skin));
		table.row();
		table.add(quitGame);
		table.row();
		
		table.pack();
		table.setX(Gdx.graphics.getWidth()/2 - table.getWidth()/2);
		table.setY(Gdx.graphics.getHeight()/2 - table.getHeight()/2);
		stage.addActor(table);
	}

	public void update() {
		stage.draw();
	}

	public void setPrevious() {
		previousInput = Gdx.input.getInputProcessor();
		previousCursorCatched = Gdx.input.isCursorCatched();
	}

	public void restorePrevious() {
		Gdx.input.setInputProcessor(previousInput);
		Gdx.input.setCursorCatched(previousCursorCatched);
	}
}
