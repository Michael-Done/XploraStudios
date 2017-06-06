package com.project_xplora.game.settings_menu_classes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Similar to the KeyField class, the KeyHandlerStage class is a helper class
 * that is used for custom TextButtons in our SettingsScene class. It is
 * particularly helpful for being able to implement re-mappable controls which
 * are present in our SettingsScene class. It creates a custom stage.
 * <p>
 * Time taken to complete: 30 minutes.
 * <p>
 * <b> Class Fields:
 * <p>
 * </b> static int <b> cachedKey </b> - Holds the cached key value.
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class KeyHandlerStage extends Stage {
	private static int cachedKey = -1;

	/** Default constructor */
	public KeyHandlerStage() {
	}

	/**
	 * Viewport-related constructor
	 * 
	 * @param viewport
	 *            - Stores the current camera manager.
	 */
	public KeyHandlerStage(Viewport viewport) {
		super(viewport);
	}

	/**
	 * Viewport and Batch - related constructor.
	 * 
	 * @param viewport
	 *            - Stores the current camera manager.
	 * @param batch
	 *            - Stores the texture region references.
	 */
	public KeyHandlerStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
	}

	/**
	 * Applies a key down event to the actor that has keyboard focus, if any,
	 * and returns true if the event was handled.
	 * 
	 * @param keyCode
	 *            - Stores the value of the cached key.
	 * @return Returns true if the key down event was handled.
	 */
	public boolean keyDown(int keyCode) {
		cachedKey = keyCode;
		return super.keyDown(keyCode);
	}

	/**
	 * Returns the value of the cached key.
	 * 
	 * @return The value of the cached key.
	 */
	public static int getCachedKey() {
		return cachedKey;
	}

	/** Resets the value of the cached key. */
	public static void resetCachedKey() {
		cachedKey = -1;
	}
}
