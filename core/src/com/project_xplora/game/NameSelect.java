/**
 * 
 */
package com.project_xplora.game;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private SpriteBatch batch;
	private Texture backgroundTexture;
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
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("nameSelect.jpg"));
		table = new Table();
		stage = new Stage();
		field = new TextField("", skin);
		field.selectAll();
		TextButton continueButton = new TextButton("Accept", skin);
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!field.getText().trim().equals("") && field.getText().length() < 21) {
					cont = true;
					name = field.getText();
					System.out.println("NAME: " + name);
				}
			}
		});
		table.add(new Label("Enter Username:", skin));
		table.row();
		table.add(field).size(800, 100);
		table.row();
		table.add(continueButton).padTop(20);
		table.pack();
		table.setX(Gdx.graphics.getWidth() / 2 - table.getWidth() / 2);
		table.setY(Gdx.graphics.getHeight() / 2 - table.getHeight() / 2);
		stage.addActor(table);
		stage.setKeyboardFocus(field);
		field.getOnscreenKeyboard().show(true);
	}

	public void update() {
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		stage.draw();
	}

	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(stage);
	}

}
