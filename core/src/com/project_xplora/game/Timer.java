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
	private ProjectXploraGame.Level scene;

	/**
	 * 
	 */
	public Timer(PlayerData player) {
		this.player = player;
	}

	public void update(ProjectXploraGame.Level currentScene, boolean pause) {
		scene = currentScene;
		switch (currentScene) {
		case EUROPE:
			if (!pause)
				player.setEuropeTime(player.getEuropeTime() + Gdx.graphics.getDeltaTime());
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

	public void total() {
		player.setTime((long) (player.getEuropeTime() + player.getBCTime() + player.getRomeTime()));
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

	public void add30() {
		switch (scene) {
		case EUROPE:
			player.setEuropeTime(player.getEuropeTime() + 30);
			break;
		case BC:
			player.setBCTime(player.getBCTime() + 30);
			break;
		case ROME:
			player.setRomeTime(player.getRomeTime() + 30);
			break;
		default:
			break;
		}
	}
}
