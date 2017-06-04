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
 * @author Michael
 *
 */
public class TreasureChest {
	public GameObject lid;
	public GameObject base;
	private boolean open;
	private float targetDeg;
	private float currentDeg;
	private boolean useRoll;
	private boolean isUnlocked = false;
	private boolean isQuiz = false;

	/**
	 * 
	 */
	public TreasureChest(float x, float y, float z, float rot) {
		AssetManager assets = new AssetManager();
		assets.load("ChestLid.g3db", Model.class);
		assets.load("ChestBase.g3db", Model.class);
		assets.load("Interact.g3db", Model.class);
		assets.finishLoading();
		lid = new GameObject(assets.get("ChestLid.g3db", Model.class), new Vector3(x, y, z + 0.823f),
				new Vector3(0, 0, 1), rot);
		base = new GameObject(assets.get("ChestBase.g3db", Model.class), new Vector3(x, y, z + 0.823f),
				new Vector3(0, 0, 1), rot);
		useRoll = rot == 90 || rot == -90;
		lid.transform.rotate(1, 0, 0, 90);
		base.transform.rotate(1, 0, 0, 90);

		open = false;
	}

	public void open() {
		open = true;
		targetDeg = 0;
	}

	public void close() {
		open = false;
		targetDeg = -90;
	}

	public void update(Vector3 camLoc) {
		if (camLoc.dst(lid.transform.getTranslation(new Vector3())) > 5) {
			open();
		} else {
			if (!isUnlocked) {
				close();
			}
		}
		if (useRoll) {
			currentDeg = lid.transform.getRotation(new Quaternion()).nor().getRoll() - 90;
		} else {
			currentDeg = lid.transform.getRotation(new Quaternion()).nor().getPitch() - 90;
		}
		if (open && currentDeg < targetDeg) {
			lid.transform.rotate(1, 0, 0, (targetDeg - currentDeg) / 4);
		} else if (!open && currentDeg > targetDeg) {
			lid.transform.rotate(1, 0, 0, (targetDeg - currentDeg) / 4);
		}
		if (open && Math.abs((targetDeg - currentDeg)) < 1 && !isUnlocked) {
			isQuiz = true;
			isUnlocked = true;
		} else {
			isQuiz = false;
		}
	}

	public boolean isQuiz() {
		return isQuiz;
	}

}
