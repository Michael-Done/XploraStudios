package com.project_xplora.game;

import java.util.Iterator;
import java.util.TreeSet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project_xplora.game.highscore.*;

/**
 * The high scores class. This class is used to display the highscores stored in
 * the ProjectXploraGame class
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * Stage <b> stage </b> - The stage
 * <p>
 * Table <b> table </b> - The table
 * <p>
 * Skin <b> skin </b> - The GUI skin
 * <p>
 * private TreeSet <b> highscores </b> - The list of highscores
 * <p>
 * public boolean <b> backtoMenu </b> - If the game should go back to the menu
 * <p>
 * Time taken to complete: 0.5 hours
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class HighScoresScene extends GameObjectController {
	private Stage stage;
	private Table table;
	private Skin skin;
	private TreeSet<PlayerData> highscores;
	public boolean backtoMenu = false;

	/**
	 * class constructor. Constructs based on settings and highscores.
	 * 
	 * @param settings
	 * @param highscores
	 */
	public HighScoresScene(Settings settings, TreeSet<PlayerData> highscores) {
		super(settings);
		loadModelInstances();
		this.highscores = highscores;
	}

	@Override
	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(stage);
		updateTable();
	}

	@Override
	public void loadModelInstances() {
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		table = new Table();
		stage.addActor(table);
	}

	/**
	 * updates the table
	 */
	public void updateTable() {
		table.clear();
		Iterator<PlayerData> i = highscores.iterator();
		int a = 0;
		table.add(new Label("NAME", skin));
		table.add(new Label("TIME", skin));
		table.row();
		while (i.hasNext() && a++ < 10) {
			PlayerData p = i.next();
			table.add(new Label(p.getPlayerName() + ": ", skin));
			table.add(new Label(p.getTime() / 60 + " Minutes", skin));
			table.row();
		}
		TextButton back = new TextButton("Back", skin);
		TextButton reset = new TextButton("Reset", skin);
		reset.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				ProjectXploraGame.highscores.clear();
			}
		});
		back.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				backtoMenu = true;
			}
		});
		table.row();
		table.add(back);
		table.add(reset);
		table.pack();
		table.setX(Gdx.graphics.getWidth() / 2 - table.getWidth() / 2);
		table.setY(Gdx.graphics.getHeight() / 2 - table.getHeight() / 2);
	}

	@Override
	public void update() {
		stage.draw();
	}
}
