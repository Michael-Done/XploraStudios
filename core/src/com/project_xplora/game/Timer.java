/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.project_xplora.game.highscore.PlayerData;

/**
 * @author Michael
 *
 */
public class Timer {
	public PlayerData player;

	/**
	 * 
	 */
	public Timer(PlayerData player) {
		this.player = player;
	}

	public void update(ProjectXploraGame.Level currentScene, boolean pause) {
		switch (currentScene) {
		case EUROPE:
			if (!pause)
				player.setEuropeTime(player.getEuropeTime() + Gdx.graphics.getDeltaTime());
			System.out.println(player.getEuropeTime());
			break;
		case BC:
			if (!pause)
				player.setBCTime(player.getBCTime() + Gdx.graphics.getDeltaTime());
			break;
		case ROME:
			if (!pause)
				player.setRomeTime(player.getRomeTime() + Gdx.graphics.getDeltaTime());
			break;
		default:
			break;

		}
	}

	public void finishEurope() {
		player.setEuropecompleted(true);
	}

	public void finishBC() {
		player.setBCCompleted(true);
	}

	public void finishRome() {
		player.setRomeCompleted(true);
	}
}
