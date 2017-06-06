package com.project_xplora.game.settings_menu_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The KeyField class is a helper class that is used for custom TextButtons in
 * our SettingsScene class. It is particularly helpful for being able to
 * implement re-mappable controls which are present in our SettingsScene class.
 * <p>
 * Time taken to complete: 30 minutes.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * enum <b> State </b> - Stores the keys that point to the state of the
 * KeyField.
 * <p>
 * State <b> state </b> - Stores the state of the KeyField class.
 * <p>
 * int <b> key </b> - Holds the key value.
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */

public class KeyField extends TextButton {
	public enum State {
		SET, WAITING
	};

	State state;
	public int key;

	public KeyField(Skin skin, int currentKey) {
		super(Keys.toString(currentKey), skin);
		state = State.SET;
		key = currentKey;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (state == State.WAITING) {
			setText("[Enter new Key]");
			if (KeyHandlerStage.getCachedKey() != -1) {
				key = KeyHandlerStage.getCachedKey();
				state = State.SET;
				KeyHandlerStage.resetCachedKey();
			}
		} else if (state == State.SET) {
			setText(Keys.toString(key));
		}
		super.draw(batch, parentAlpha);
	}

	/** Sets the keyfield's state to waiting. */
	public void setToWaiting() {
		state = State.WAITING;
	}

	/**
	 * Gets the key value.
	 * 
	 * @return - Returns the key value.
	 */
	public int getKey() {
		return key;
	}
}
