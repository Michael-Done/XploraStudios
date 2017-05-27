/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.project_xplora.game.settings_menu_classes.KeyField;
import com.project_xplora.game.settings_menu_classes.KeyHandlerStage;

/**
 * @author Michael
 *
 */
public class SettingsScene extends GameObjectController {
	SpriteBatch batch;
	Stage stage;
	Skin skin;
	Table table;
	Settings tempSettings;

	/**
	 * the choice made when exiting the settings menu. -1 for cancel, 1 for
	 * apply.
	 */
	private int choice = 0;

	final TextButton forward;
	final TextButton backward;
	final TextButton left;
	final TextButton right;
	final TextButton interact;
	final TextButton pause;

	TextButton apply;
	TextButton cancel;

	Slider volume;
	Slider mouseSens;

	ApplyCancelListener applyListener;
	ApplyCancelListener cancelListener;

	/**
	 * @param settings
	 */
	public SettingsScene(Settings currentSettings) {
		super(currentSettings);
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new KeyHandlerStage();
		table = new Table();
		tempSettings = currentSettings;
		forward = new KeyField(skin, tempSettings.getForward());
		backward = new KeyField(skin, tempSettings.getBackward());
		left = new KeyField(skin, tempSettings.getStrafe_left());
		right = new KeyField(skin, tempSettings.getStrafe_right());
		interact = new KeyField(skin, tempSettings.getInteract());
		pause = new KeyField(skin, tempSettings.getPause());
		// loadModelInstances();

	}

	@Override
	public void loadModelInstances() {
		table.clear();
		skin.getFont("font-label").getData().setScale(Gdx.graphics.getWidth()/6400.0f, Gdx.graphics.getHeight()/3600.0f);
		skin.getFont("font-button").getData().setScale(Gdx.graphics.getWidth()/6400.0f, Gdx.graphics.getHeight()/3600.0f);



		volume = new Slider(0, 100, 1, false, skin);
		volume.setValue(tempSettings.getMasterVolume());
		mouseSens = new Slider(0, 100, 1, false, skin);
		mouseSens.setValue(tempSettings.getMouseSens());

		applyListener = new ApplyCancelListener(true);
		cancelListener = new ApplyCancelListener(false);

		apply = new TextButton("Apply", skin);
		cancel = new TextButton("Cancel", skin);

		apply.addListener(applyListener);
		cancel.addListener(cancelListener);

		forward.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((KeyField) (forward)).setToWaiting();
			}
		});
		backward.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((KeyField) (backward)).setToWaiting();
			}
		});
		left.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((KeyField) (left)).setToWaiting();
			}
		});
		right.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((KeyField) (right)).setToWaiting();
			}
		});
		interact.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((KeyField) (interact)).setToWaiting();
			}
		});
		pause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((KeyField) (pause)).setToWaiting();
			}
		});

		Label header1 = new Label("KEYBINDINGS", skin);
		header1.setFontScale(1.1f);

		Label header2 = new Label("CONTROLS", skin);
		header2.setFontScale(1.1f);

		Label header3 = new Label("AUDIO", skin);
		header3.setFontScale(1.1f);

		Label text = new Label("Forward", skin);
		// Keybinding settings
		table.add(header1);
		table.row();
		table.add(new Label("Forward", skin)).width(300).padTop(30);
		table.add(forward).width(600);
		table.row();
		text = new Label("Backward", skin);
		table.add(text).width(300).padTop(30);
		table.add(backward).width(600);
		table.row();
		text = new Label("Left", skin);
		table.add(text).width(300).padTop(30);
		table.add(left).width(600);
		table.row();
		text = new Label("Right", skin);
		table.add(text).width(300).padTop(30);
		table.add(right).width(600);
		table.row();
		text = new Label("Interact", skin);
		table.add(text).width(300).padTop(30);
		table.add(interact).width(600);
		table.row();
		text = new Label("Pause", skin);
		table.add(text).width(300).padTop(30);
		table.add(pause).width(600);
		table.row();
		// Control settings
		table.add(header2).padTop(50);
		table.row();
		text = new Label("Mouse Sensitivity", skin);
		table.add(text).width(300).padTop(30);
		table.add(mouseSens).width(600);
		table.row();
		// Audio Settings
		table.add(header3).padTop(50);
		table.row();
		text = new Label("Volume", skin);
		table.add(text).width(300).padTop(30);
		table.add(volume).width(600);
		table.row();
		// Apply/cancel buttons
		table.add(apply).width(200).padTop(40);
		table.add(cancel).width(200).padTop(40);
		// table.setDebug(true);
		table.setX(Gdx.graphics.getWidth() / 2 - 500);
		table.setY(Gdx.graphics.getHeight() / 2 - 500);
		table.pack();
		table.center();
		stage.addActor(table);
	}

	public void update() {
		batch.begin();
		stage.draw();
		batch.end();

		choice = -((int) (Math.signum(cancelListener.getChoice())));
		if (applyListener.getChoice() != 0) {
			choice = applyListener.getChoice();
		}

	}

	public void setInputProccessor() {
		Gdx.input.setCursorCatched(false);
		Gdx.input.setInputProcessor(stage);
	}

	public void resetChoice() {
		choice = 0;
	}

	public int getChoice() {
		return choice;
	}

	public Settings getNewSettings() {
		tempSettings.setForward(((KeyField) forward).getKey());
		tempSettings.setBackward(((KeyField) backward).getKey());
		tempSettings.setStrafe_left(((KeyField) left).getKey());
		tempSettings.setStrafe_right(((KeyField) right).getKey());
		tempSettings.setInteract(((KeyField) interact).getKey());
		tempSettings.setPause(((KeyField) pause).getKey());
		tempSettings.setMouseSens((int) mouseSens.getValue());
		tempSettings.setMasterVolume((int) volume.getValue());
		return tempSettings;
	}

	@Override
	public void disposeAll() {
		batch.dispose();
		stage.dispose();
		skin.dispose();
		table = null;
	}

	class ApplyCancelListener extends ClickListener {
		private boolean apply;
		private int choice;

		public ApplyCancelListener(boolean apply) {
			this.apply = apply;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			choice = apply ? 1 : -1;
		}

		public int getChoice() {
			return choice;
		}
	}
}
