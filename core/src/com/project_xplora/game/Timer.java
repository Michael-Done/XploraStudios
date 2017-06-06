/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.project_xplora.game.highscore.PlayerData;

/**
 * The timer class. Used to keep track of the player's time and their game
 * progress.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * public PlayerData <b> player </b> - The data for the current player
 * <p>
 * private int <b> scene </b> - The current scene represented as an int
 * <p>
 * Time taken to complete: 20 mins
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class Timer {
	/** The data for the current player */
	public PlayerData player;
	/** The current scene represented as an int */
	private int scene = 0;

	/**
	 * basic constructor that takes in a new PlayerData to modify.
	 * 
	 * @param player
	 */
	public Timer(PlayerData player) {
		this.player = player;
	}

	/**
	 * updates the timer
	 * 
	 * @param currentScene
	 * @param pause
	 */
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

	/**
	 * totals the player's score
	 */
	public void total() {
		player.setTime((long) (player.getEuropeTime() + player.getBCTime() + player.getRomeTime()));
	}

	/**
	 * adds a 30 sec time penalty
	 */
	public void add30() {
		if (scene == 0) {
			player.setEuropeTime(player.getEuropeTime() + 30);
		}
		if (scene == 1) {
			player.setBCTime(player.getBCTime() + 30);
		}
		if (scene == 2) {
			player.setRomeTime(player.getRomeTime() + 30);
		}
	}
}
