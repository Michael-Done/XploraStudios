package com.project_xplora.game.highscore;

import java.io.Serializable;

import com.badlogic.gdx.utils.Array;

/**
 * The PlayerData class is a helper class for the high scores portion of the
 * Project Xplorer Game. Not only does it store relevant information such as
 * player names and time taken to complete worlds, but this class also
 * implements the serializable and comparable interfaces, which allows for the
 * usage of a TreeSet in our high scores.
 * <p>
 * Time taken to complete: 1 hour.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * final long <b> serialVersionUID </b> - Used to prevent serializable class
 * warnings.
 * <p>
 * float <b> romeTime </b> - The Player's time taken to complete the Rome level.
 * <p>
 * float <b> europeTime </b> - The Player's time taken to complete the Europe
 * level.
 * <p>
 * float <b> BCTime </b> - The Player's time taken to complete the BCTime level.
 * <p>
 * boolean <b> BCCompleted </b> - True if the player has completed the BC level.
 * <p>
 * boolean <b> europeCompleted </b> - True if the player has completed the
 * Europe level.
 * <p>
 * boolean <b> romeCompleted </b> - True if the player has completed the Rome
 * level.
 * <p>
 * long <b> finalTime </b> - Stores the player's time taken to complete the game
 * in minutes. String <b> playerName </b> - Stores the player's name.
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class PlayerData implements Serializable, Comparable<PlayerData> {
	private static final long serialVersionUID = -8800684059097726847L;
	private float romeTime;
	private float europeTime;
	private float BCTime;
	private boolean BCCompleted;
	private boolean europeCompleted;
	private boolean romeCompleted;
	private long finalTime;
	private String playerName;

	/**
	 * Class Constructor: Initalizes all fields with default values, except for
	 * the name, which is assigned to the given parameter.
	 * 
	 * @param name
	 *            - The name the will be assigned to the player.
	 */
	public PlayerData(String name) {
		romeTime = 0;
		europeTime = 0;
		BCTime = 0;
		BCCompleted = false;
		europeCompleted = false;
		romeCompleted = false;
		finalTime = 0;
		playerName = name;
	}

	@Override
	public int compareTo(PlayerData other) {
		if (other != null && other.playerName != null && this.finalTime == other.finalTime) {
			System.out.println(other.playerName);
			return this.playerName.compareTo(other.playerName);
		}
		return (int) (this.finalTime - other.finalTime);
	}

	/**
	 * @return the romeTime
	 */
	public float getRomeTime() {
		return romeTime;
	}

	/**
	 * @param romeTime
	 *            the romeTime to set
	 */
	public void setRomeTime(float romeTime) {
		this.romeTime = romeTime;
	}

	/**
	 * @return the europeTime
	 */
	public float getEuropeTime() {
		return europeTime;
	}

	/**
	 * @param f
	 *            the europeTime to set
	 */
	public void setEuropeTime(float f) {
		this.europeTime = f;
	}

	/**
	 * @return the bCTime
	 */
	public float getBCTime() {
		return BCTime;
	}

	/**
	 * @param bCTime
	 *            the bCTime to set
	 */
	public void setBCTime(float bCTime) {
		BCTime = bCTime;
	}

	/**
	 * @return the bCCompleted
	 */
	public boolean isBCCompleted() {
		return BCCompleted;
	}

	/**
	 * @param bCCompleted
	 *            the bCCompleted to set
	 */
	public void setBCCompleted(boolean bCCompleted) {
		BCCompleted = bCCompleted;
	}

	/**
	 * @return the europeCompleted
	 */
	public boolean isEuropecompleted() {
		return europeCompleted;
	}

	/**
	 * @param europecompleted
	 *            the europeCompleted to set
	 */
	public void setEuropecompleted(boolean europecompleted) {
		this.europeCompleted = europecompleted;
	}

	/**
	 * @return the romeCompleted
	 */
	public boolean isRomeCompleted() {
		return romeCompleted;
	}

	/**
	 * @param romeCompleted
	 *            the romeCompleted to set
	 */
	public void setRomeCompleted(boolean romeCompleted) {
		this.romeCompleted = romeCompleted;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return finalTime;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.finalTime = time;
	}

	/** Returns the name of the player. */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Sets the name of the player to the given parameter.
	 * 
	 * @param playerName
	 *            - Name to be assigned.
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
