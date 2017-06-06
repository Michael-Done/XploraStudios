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
 * The GameCompleted class. This is the scene that dispalys when the user
 * completes the game
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * Stage <b> stage </b> - The stage
 * <p>
 * Table <b> table </b> - The table
 * <p>
 * Skin <b> skin </b> - The GUI skin
 * <p>
 * Time taken to complete: 0.5 hours
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class GameCompletedScene extends GameObjectController {
	public boolean nextScene = false;
	private Stage stage;
	private Table table;
	private Skin skin;

	/**
	 * Class constructor.
	 * 
	 * @param settings
	 */
	public GameCompletedScene(Settings settings) {
		super(settings);
		loadModelInstances();
	}

	@Override
	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(stage);
		ProjectXploraGame.timer.total();
	}

	@Override
	public void loadModelInstances() {
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		table = new Table();
	}

	/** updates the table */
	public void updateTable() {
		TextButton next = new TextButton("Continue", skin);
		next.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextScene = true;
			}
		});
		table.clear();
		table.add(
				new Label("Rome Time taken: " + ProjectXploraGame.timer.player.getRomeTime() / 60 + " Minutes", skin));
		table.row();
		table.add(new Label("Europe Time taken: " + ProjectXploraGame.timer.player.getEuropeTime() / 60.0f + " Minutes",
				skin));
		table.row();
		table.add(new Label(
				"British Columbia Time taken: " + ProjectXploraGame.timer.player.getBCTime() / 60.0f + " Minutes",
				skin));
		table.row();
		table.add(
				new Label("Total Time taken: " + ProjectXploraGame.timer.player.getTime() / 60.0f + " Minutes", skin));
		table.row();
		table.add(next);
		table.pack();
		table.setX(Gdx.graphics.getWidth() / 2 - table.getWidth() / 2);
		table.setY(Gdx.graphics.getHeight() / 2 - table.getHeight() / 2);
		stage.addActor(table);
	}

	@Override
	public void update() {
		stage.draw();
	}

}
