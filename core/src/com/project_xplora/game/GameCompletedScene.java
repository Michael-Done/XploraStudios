/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
public class GameCompletedScene extends GameObjectController {
	public boolean nextScene = false;
	private Stage stage;
	private Table table;
	private Skin skin;
	/**
	 * @param settings
	 */
	public GameCompletedScene(Settings settings) {
		super(settings);
		loadModelInstances();
	}
	public void camSetup(){
		Gdx.input.setCursorCatched(false);
	}
	public void loadModelInstances(){
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		table = new Table();
		TextButton next = new TextButton("Continue", skin);
		next.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextScene = true;
			}
		});
		table.addActor(new Label("Rome Time taken: " + ProjectXploraGame.currentPlayer.getRomeTime()/60 + " Minutes", skin));
		table.row();
		table.addActor(new Label("Europe Time taken: " + ProjectXploraGame.currentPlayer.getEuropeTime()/60 + " Minutes", skin));
		table.row();
		table.addActor(new Label("British Columbia Time taken: " + ProjectXploraGame.currentPlayer.getBCTime()/60 + " Minutes", skin));
		table.row();
		table.addActor(new Label("Total Time taken: " + ProjectXploraGame.currentPlayer.getTime()/60 + " Minutes", skin));
		table.row();
		table.addActor(next);
		table.pack();
		table.setX(Gdx.graphics.getWidth()/2 - table.getWidth()/2);
		table.setY(Gdx.graphics.getHeight()/2 - table.getHeight()/2);
		
		stage.addActor(table);
	}
	public void update(){
		stage.draw();
	}
	

}
