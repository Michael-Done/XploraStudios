package com.project_xplora.game.highscore;

import java.io.Serializable;

import com.badlogic.gdx.utils.Array;

public class PlayerData implements Serializable, Comparable<PlayerData> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8800684059097726847L;
	/** The Player's time taken to complete the rome level */
	private long romeTime;
	/** The Player's time taken to complete the europe level */
	private long europeTime;
	/** The Player's time taken to complete the BC level */
	private long BCTime;
	/** True if the player has completed the BC level */
	private boolean BCCompleted;
	/** True if the player has completed the europe level */
	private boolean europeCompleted;
	/** True if the player has completed the rome level */
	private boolean romeCompleted;
	/** The Player's time taken to complete the game in minutes */
	private long finalTime;
	/** The player's name */
	private String playerName;
	
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
		if(this.finalTime == other.finalTime){
			return this.playerName.compareTo(other.playerName);
		}
		return (int) (this.finalTime - other.finalTime);
	}

	/**
	 * @return the romeTime
	 */
	public long getRomeTime() {
		return romeTime;
	}

	/**
	 * @param romeTime the romeTime to set
	 */
	public void setRomeTime(long romeTime) {
		this.romeTime = romeTime;
	}

	/**
	 * @return the europeTime
	 */
	public long getEuropeTime() {
		return europeTime;
	}

	/**
	 * @param europeTime the europeTime to set
	 */
	public void setEuropeTime(long europeTime) {
		this.europeTime = europeTime;
	}

	/**
	 * @return the bCTime
	 */
	public long getBCTime() {
		return BCTime;
	}

	/**
	 * @param bCTime the bCTime to set
	 */
	public void setBCTime(long bCTime) {
		BCTime = bCTime;
	}

	/**
	 * @return the bCCompleted
	 */
	public boolean isBCCompleted() {
		return BCCompleted;
	}

	/**
	 * @param bCCompleted the bCCompleted to set
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
	 * @param europecompleted the europeCompleted to set
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
	 * @param romeCompleted the romeCompleted to set
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
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.finalTime = time;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
