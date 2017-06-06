package com.project_xplora.game;

import java.io.Serializable;
import com.badlogic.gdx.Input.Keys;

/**
 * The Settings class is Project Xplorer's settings class framework. Although
 * this class is not responsible for setting up the menu scene, it does behind
 * the scenes work such as all the implementation of the settings.
 * <p>
 * Time taken to complete: 1 hour.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * final long <b> serialVersionUID </b> - Used to prevent serializable class
 * warnings.
 * <p>
 * int <b> masterVolume </b> - Stores the volume setting.
 * <p>
 * int <b> forward </b> - Stores the key code for the forward button.
 * <p>
 * int <b> backward </b> - Stores the key code for the backward button.
 * <p>
 * int <b> strafe_left </b> - Stores the key code for the strafe left button.
 * <p>
 * int <b> strafe_right </b> - Stores the key code for the strafe right button.
 * <p>
 * int <b> interact </b> - Stores the key code for the interact button.
 * <p>
 * int <b> pause </b> - Stores the key code for the pause button.
 * <p>
 * int <b> mouseSens </b> - Stores the mouse sensitivity setting.
 * <p>
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class Settings implements Serializable {
	private static final long serialVersionUID = 5955953593712911600L;
	private int masterVolume;
	private int forward;
	private int backward;
	private int strafe_left;
	private int strafe_right;
	private int interact;
	private int pause;
	private int mouseSens;

	/**
	 * Default Constructor: calls overloaded constructor with default control
	 * scheme values.
	 */
	public Settings() {
		this(100, Keys.W, Keys.S, Keys.A, Keys.D, Keys.E, Keys.ESCAPE, 50);
	}

	/**
	 * Settings overloaded constructor, calls overloaded constructor with values
	 * from settings parameter.
	 * 
	 * @param settings
	 *            - Stores the current settings.
	 */
	public Settings(Settings settings) {
		this(settings.getMasterVolume(), settings.getForward(), settings.getBackward(), settings.getStrafe_left(),
				settings.getStrafe_right(), settings.getInteract(), settings.getPause(), settings.getMouseSens());
	}

	/**
	 * Second settings overloaded constructor. Sets values to according
	 * parameters.
	 * 
	 * @param masterVolume
	 * @param forward
	 * @param backward
	 * @param strafe_left
	 * @param strafe_right
	 * @param interact
	 * @param pause
	 * @param mouseSens
	 */
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

	/** @return Returns master volume setting */
	public int getMasterVolume() {
		return masterVolume;
	}

	/**
	 * Sets the master volume setting according to the parameter passed.
	 * 
	 * @param masterVolume
	 */
	public void setMasterVolume(int masterVolume) {
		this.masterVolume = masterVolume;
	}

	/** @return Returns forward button key code. */
	public int getForward() {
		return forward;
	}

	/**
	 * Sets the forward button according to the key code passed in the
	 * parameter.
	 * 
	 * @param forward
	 */
	public void setForward(int forward) {
		this.forward = forward;
	}

	/** @return Returns backward button key code. */
	public int getBackward() {
		return backward;
	}

	/**
	 * Sets the backward button according to the key code passed in the
	 * parameter.
	 * 
	 * @param backward
	 */
	public void setBackward(int backward) {
		this.backward = backward;
	}

	/** @return Returns strafe_left button key code. */
	public int getStrafe_left() {
		return strafe_left;
	}

	/**
	 * Sets the strafe_left button according to the key code passed in the
	 * parameter.
	 * 
	 * @param strafe_left
	 */
	public void setStrafe_left(int strafe_left) {
		this.strafe_left = strafe_left;
	}

	/** @return Returns strafe_right button key code. */
	public int getStrafe_right() {
		return strafe_right;
	}

	/**
	 * Sets the strafe_right button according to the key code passed in the
	 * parameter.
	 * 
	 * @param strafe_right
	 */
	public void setStrafe_right(int strafe_right) {
		this.strafe_right = strafe_right;
	}

	/** @return Returns interact button key code. */
	public int getInteract() {
		return interact;
	}

	/**
	 * Sets the interact button according to the key code passed in the
	 * parameter.
	 * 
	 * @param interact
	 */
	public void setInteract(int interact) {
		this.interact = interact;
	}

	/** @return Returns pause button key code. */
	public int getPause() {
		return pause;
	}

	/**
	 * Sets the pause button according to the key code passed in the parameter.
	 * 
	 * @param pause
	 */
	public void setPause(int pause) {
		this.pause = pause;
	}

	/** @return Returns mouse sensitivity setting. */
	public int getMouseSens() {
		return mouseSens;
	}

	/**
	 * Sets the mouse sensitivity setting according to the parameter passed.
	 * 
	 * @param mouseSens
	 */
	public void setMouseSens(int mouseSens) {
		this.mouseSens = mouseSens;
	}
}
