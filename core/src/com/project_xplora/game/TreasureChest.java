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
	public GameObject artifact;
	public GameObject description;
	private boolean open;
	private float targetDeg;
	private float currentDeg;
	private boolean useRoll;
	private boolean isUnlocked = false;
	private boolean isQuiz = false;
	private int signum;
	public boolean added = false;
	/**
	 * 
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
		artifactTargetZ = z;
		open = false;
		this.description = new GameObject(description, new Vector3(x, y, z));
		this.description.transform.rotate(0, 1, 0, rot);
	}

	public void open() {
		open = true;
		targetDeg = -90;
	}

	public void close() {
		open = false;
		targetDeg = 0;
	}
	public boolean unlocked(){
		return isUnlocked;
	}
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
			currentDeg = signum*lid.transform.getRotation(new Quaternion()).nor().getRoll() - 90;
		} else {
			currentDeg = signum*lid.transform.getRotation(new Quaternion()).nor().getPitch() - 90;
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
		if(isUnlocked && !isQuiz){
			artifactTargetZ += 2;
		}
		
	}

	public boolean isQuiz() {
		return isQuiz;
	}
	public Vector2 location2 (){
		return new Vector2(lid.transform.getTranslation(new Vector3()).x, lid.transform.getTranslation(new Vector3()).y);
	}
}
