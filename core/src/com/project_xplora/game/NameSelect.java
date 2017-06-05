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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * @author Michael
 *
 */
public class NameSelect extends GameObjectController {
	private Skin skin;
	private Table table;
	private Stage stage;
	public String name = "";
	public boolean cont = false;
	TextField field;

	/**
	 * @param settings
	 */
	public NameSelect(Settings settings) {
		super(settings);
		loadModelInstances();
		// TODO Auto-generated constructor stub
	}

	public void loadModelInstances() {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		table = new Table();
		stage = new Stage();
		field = new TextField("name", skin);
		field.selectAll();
		TextButton continueButton = new TextButton("Accept", skin);
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!field.getText().equals("")) {
					cont = true;
					name = field.getMessageText();
				}
			}
		});
		table.add(new Label("ENTER USERNAME:", skin));
		table.row();
		table.add(field).size(800, 100);
		table.row();
		table.add(continueButton);
		table.pack();
		table.setX(Gdx.graphics.getWidth() / 2 - table.getWidth() / 2);
		table.setY(Gdx.graphics.getHeight() / 2 - table.getHeight() / 2);
		stage.addActor(table);
	}

	public void update() {
		stage.draw();
	}

	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(stage);
	}

}
