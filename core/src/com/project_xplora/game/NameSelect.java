package com.project_xplora.game;

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
 * The NameSelect class is Project Xplorer's name select screen manager.
 * <p>
 * Time taken to complete: 20 minutes.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * Skin <b> skin </b> - Creates a Skin for the class.
 * <p>
 * SpriteBatch <b> batch </b> - Creates a SpriteBatch for the class.
 * <p>
 * Texture <b> backgroundTexture </b> - Stores a Texture for the class.
 * <p>
 * Table <b> table </b> - Creates a Table for the class.
 * <p>
 * Stage <b> stage </b> - Creates a Stage for the class.
 * <p>
 * String <b> name </b> - Stores the user's name.
 * <p>
 * boolean <b> cont </b> - Whether the game should proceed or not.
 * <p>
 * TextField <b> field </b> - Used to create a textfield that the user can type
 * in.
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
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
	 * Class Constructor: Initializes object with current settings as well as
	 * loads in all model instances.
	 * 
	 * @param settings
	 *            - Stores the current settings.
	 */
	public NameSelect(Settings settings) {
		super(settings);
		loadModelInstances();
	}

	/**
	 * Initializes fields, assigns click listeners to buttons, creates table,
	 * adds table to stage and sets focus to textfield.
	 */
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

	/**
	 * Updates the libGDX game client screen. This method is called very
	 * frequently in order to update the screen. It also manages what is to be
	 * drawn for the name select screen.
	 */
	public void update() {
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		stage.draw();
	}

	/** Overrides the camera setup. */
	public void camSetup() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(stage);
	}

}
