/**
 * 
 */
package com.project_xplora.game;

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
	public class PauseStage extends Stage{
		public boolean paused;
		@Override
		public boolean keyDown(int keyCode){
			super.keyDown(keyCode);
			if(keyCode == PAUSE){
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
		quitGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		table.clear();
		table.add(new Label("TESTING", skin));
		table.add(new TextButton("Quit Game", skin));
		table.row();
		table.pack();
		stage.addActor(table);
	}
	public void update(){
		stage.draw();
	}
	public void setPrevious(){
		previousInput = Gdx.input.getInputProcessor();
		previousCursorCatched = Gdx.input.isCursorCatched();
	}
	public void restorePrevious(){
		Gdx.input.setInputProcessor(previousInput);
		Gdx.input.setCursorCatched(previousCursorCatched);
	}
}
