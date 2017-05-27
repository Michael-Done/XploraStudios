package com.project_xplora.game.settings_menu_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class KeyField extends TextButton{
	public enum State {SET, WAITING};
	State state;
	public int key;
	public KeyField(Skin skin, int currentKey) {
		super(Keys.toString(currentKey), skin);
		state = State.SET;
		key = currentKey;
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha){
		if(state == State.WAITING) {
			setText("[Enter new Key]");
			if(KeyHandlerStage.getCachedKey() != -1){
				key = KeyHandlerStage.getCachedKey();
				state = State.SET;
				KeyHandlerStage.resetCachedKey();
			}
		} else if(state == State.SET){
			setText(Keys.toString(key));
		}
		super.draw(batch, parentAlpha);
	}
	public void setToWaiting(){
		state = State.WAITING;
	}
	public int getKey(){
		return key;
	}
}
