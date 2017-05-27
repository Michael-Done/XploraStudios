package com.project_xplora.game.settings_menu_classes;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class KeyHandlerStage extends Stage {
	private static int cachedKey = -1;
	public KeyHandlerStage() {
		// TODO Auto-generated constructor stub
	}

	public KeyHandlerStage(Viewport viewport) {
		super(viewport);
		// TODO Auto-generated constructor stub
	}

	public KeyHandlerStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
		// TODO Auto-generated constructor stub
	}
	public boolean keyDown(int keyCode){
		cachedKey = keyCode;
		return super.keyDown(keyCode);
	}
	public static int getCachedKey(){
		return cachedKey;
	}
	public static void resetCachedKey(){
		cachedKey = -1;
	}
}
