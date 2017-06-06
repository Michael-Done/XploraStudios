/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * The treasure chest class. This class is used in all 3 of the levels in order
 * to help organize and animate the treasure chests.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * public GameObject <b> lid </b> - The chest lid game object
 * <p>
 * public GameObject <b> base</b> - The chest base game object
 * <p>
 * public GameObject <b> artifact </b> - The chest artifact game object
 * <p>
 * public GameObject <b> description </b> - The chest description card game
 * object
 * <p>
 * private boolean <b> open </b> - Whether or not the chest is open
 * <p>
 * private boolean <b> useRoll </b> - Whether or not to use the roll of the lid
 * instead of the pitch
 * <p>
 * private boolean <b> isUnlocked </b> - Whether or not the chest is unlocked
 * <p>
 * private boolean <b> isQuiz </b> - Whether or not the quiz is running
 * <p>
 * private boolean <b> isAdded </b> - Whether or not the artifact and
 * description are added to the objects
 * <p>
 * private float <b> targetDeg </b> - The target rotation for the lid
 * <p>
 * private float <b> currentDeg </b> - The current rotation for the lid
 * <p>
 * private int <b> signum </b> - whether or not to use the negative of the lid
 * rotation
 * <p>
 * Time taken to complete: 1.5 hours
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class TreasureChest {
	/** The chest lid game object */
	public GameObject lid;
	/** The chest base game object */
	public GameObject base;
	/** The chest artifact game object */
	public GameObject artifact;
	/** The chest artifact decription game object */
	public GameObject description;
	/** Whether or not the chest is open */
	private boolean open;
	/** The target rotation for the lid */
	private float targetDeg;
	/** The current rotation for the lid */
	private float currentDeg;
	/** Whether or not to use the roll of the lid instead of the pitch */
	private boolean useRoll;
	/** Whether or not the chest is unlocked */
	private boolean isUnlocked = false;
	/** Whether or not the quiz is running */
	private boolean isQuiz = false;
	/** whether or not to use the negative of the lid rotation */
	private int signum;
	/** Whether or not the artifact and description are added to the objects */
	public boolean added = false;

	/**
	 * Class constructor. constructs a new chest given the following parameters:
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param rot
	 * @param model
	 * @param description
	 */
	public TreasureChest(float x, float y, float z, float rot, Model model, Model description) {
		AssetManager assets = new AssetManager();
		assets.load("ChestLid.g3db", Model.class);
		assets.load("ChestBase.g3db", Model.class);
		assets.load("Interact.g3db", Model.class);
		assets.finishLoading();
		artifact = new GameObject(model, new Vector3(x, y, z + 2));
		artifact.transform.rotate(new Vector3(0, 1, 0), rot);
		lid = new GameObject(assets.get("ChestLid.g3db", Model.class), new Vector3(x, y, z + 0.823f),
				new Vector3(0, 0, 1), rot);
		base = new GameObject(assets.get("ChestBase.g3db", Model.class), new Vector3(x, y, z + 0.823f),
				new Vector3(0, 0, 1), rot);
		useRoll = rot == 90 || rot == -90;
		lid.transform.rotate(1, 0, 0, 90);
		base.transform.rotate(1, 0, 0, 90);
		signum = (useRoll && rot > 1) || rot == 0 ? 1 : -1;
		open = false;
		this.description = new GameObject(description, new Vector3(x, y, z));
		this.description.transform.rotate(0, 1, 0, rot);
	}

	/** opens the chest */
	public void open() {
		open = true;
		targetDeg = -90;
	}

	/** closes the chest */
	public void close() {
		open = false;
		targetDeg = 0;
	}

	/**
	 * Gets the unlocked state
	 * 
	 * @return
	 */
	public boolean unlocked() {
		return isUnlocked;
	}

	/**
	 * Updates the chest. uses if statements to determine that to change when
	 * updated.
	 * 
	 * @param camLoc
	 */
	public void update(Vector3 camLoc) {
		if (camLoc.dst(lid.transform.getTranslation(new Vector3())) < 5) {
			open();
		} else {
			if (!isUnlocked) {
				close();
			}
		}
		System.out.println(targetDeg);
		if (useRoll) {
			currentDeg = signum * lid.transform.getRotation(new Quaternion()).nor().getRoll() - 90;
		} else {
			currentDeg = signum * lid.transform.getRotation(new Quaternion()).nor().getPitch() - 90;
		}
		if (open && currentDeg > targetDeg) {
			lid.transform.rotate(1, 0, 0, (targetDeg - currentDeg) / 4);
		} else if (!open && currentDeg < targetDeg) {
			lid.transform.rotate(1, 0, 0, (targetDeg - currentDeg) / 4);
		}
		if (open && Math.abs((targetDeg - currentDeg)) < 2 && !isUnlocked) {
			isQuiz = true;
			isUnlocked = true;
		} else {
			isQuiz = false;
		}

	}

	/**
	 * Checks if the quiz is running
	 * 
	 * @return isQuiz
	 */

	public boolean isQuiz() {
		return isQuiz;
	}

	/**
	 * @return the x, y position of the chest
	 */
	public Vector2 location2() {
		return new Vector2(lid.transform.getTranslation(new Vector3()).x,
				lid.transform.getTranslation(new Vector3()).y);
	}
}
