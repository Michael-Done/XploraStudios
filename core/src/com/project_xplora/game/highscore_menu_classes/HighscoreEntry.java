package com.project_xplora.game.highscore_menu_classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HighscoreEntry {
	String username;
	long time;
	BitmapFont font;
	public HighscoreEntry(String username, long time) {
		this.username = username;
		this.time = time;
		font = new BitmapFont();
	}
	
}
