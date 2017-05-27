/**
 *  XploraStudios
 *  Ms. Krasteva
 *  Settings.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
package com.project_xplora.game;

import java.io.Serializable;

import com.badlogic.gdx.Input.Keys;

public class Settings implements Serializable{
	private static final long serialVersionUID = 5955953593712911600L;
	private int masterVolume;
	private int forward;
	private int backward;
	private int strafe_left;
	private int strafe_right;
	private int interact;
	private int pause;
	private int mouseSens;

	public Settings() {
		this(100, Keys.W, Keys.S, Keys.A, Keys.D, Keys.E, Keys.ESCAPE, 50);
	}

	public Settings(int masterVolume, int forward, int backward, int strafe_left, int strafe_right, int interact,
			int pause, int mouseSens) {
		this.masterVolume = masterVolume;
		this.forward = forward;
		this.backward = backward;
		this.strafe_left = strafe_left;
		this.strafe_right = strafe_right;
		this.interact = interact;
		this.pause = pause;
		this.setMouseSens(mouseSens);
	}

	public int getMasterVolume() {
		return masterVolume;
	}

	public void setMasterVolume(int masterVolume) {
		this.masterVolume = masterVolume;
	}

	public int getForward() {
		return forward;
	}

	public void setForward(int forward) {
		this.forward = forward;
	}

	public int getBackward() {
		return backward;
	}

	public void setBackward(int backward) {
		this.backward = backward;
	}

	public int getStrafe_left() {
		return strafe_left;
	}

	public void setStrafe_left(int strafe_left) {
		this.strafe_left = strafe_left;
	}

	public int getStrafe_right() {
		return strafe_right;
	}

	public void setStrafe_right(int strafe_right) {
		this.strafe_right = strafe_right;
	}

	public int getInteract() {
		return interact;
	}

	public void setInteract(int interact) {
		this.interact = interact;
	}

	public int getPause() {
		return pause;
	}

	public void setPause(int pause) {
		this.pause = pause;
	}

	public int getMouseSens() {
		return mouseSens;
	}

	public void setMouseSens(int mouseSens) {
		this.mouseSens = mouseSens;
	}
}
