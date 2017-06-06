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
	private int scene = 0;

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
			scene = 0;
			break;
		case BC:
			if (!pause)
				player.setBCTime(player.getBCTime() + Gdx.graphics.getDeltaTime());
			scene = 1;
			break;
		case ROME:
			if (!pause)
				player.setRomeTime(player.getRomeTime() + Gdx.graphics.getDeltaTime());
			scene = 2;
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
		if(scene == 0){
			player.setEuropeTime(player.getEuropeTime() + 30);
		}if(scene == 1){
			player.setBCTime(player.getBCTime() + 30);
		}if(scene == 2){
			player.setRomeTime(player.getRomeTime() + 30);
		}
	}
}
