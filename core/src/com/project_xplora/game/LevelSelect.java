package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;

public class LevelSelect extends GameObjectController {

	public LevelSelect(Settings settings) {
		super(settings);
		initalize();
	}

	ModelInstance romeText_inst;
	ModelInstance BCText_inst;
	ModelInstance europeText_inst;
	ModelInstance exitText_inst;

	private int levelChoice = -1;

	public void resetLevelChoice() {
		levelChoice = -1;
	}

	public int getLevelChoice() {
		return levelChoice;
	}

	@Override
	public void update() {
		/*
		 * Index : Model | 0 : sky dome | 1-12 : grass | 13 : ground | 14 : exit
		 * sign | 15 :
		 * 
		 */
		updateCamera();
		boolean lookingAtExit = cameraController.getXYAngle() > 150 || cameraController.getXYAngle() < -150;
		// System.out.println(cameraController.getXYAngle());
		boolean lookingAtRome = cameraController.getXYAngle() > 30 && cameraController.getXYAngle() < 90;
		boolean lookingAtBC = cameraController.getXYAngle() > -30 && cameraController.getXYAngle() < 30;
		boolean lookingAtEurope = cameraController.getXYAngle() > -90 && cameraController.getXYAngle() < -30;

		if (lookingAtRome) {
			objects.add(romeText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				levelChoice = 0;
			}
		} else {
			while (objects.contains(romeText_inst, true))
				objects.removeValue(romeText_inst, true);
		}

		if (lookingAtBC) {
			objects.add(BCText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				levelChoice = 1;
			}
		} else {
			while (objects.contains(BCText_inst, true))
				objects.removeValue(BCText_inst, true);
		}

		if (lookingAtEurope) {
			objects.add(europeText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				levelChoice = 2;
			}
		} else {
			while (objects.contains(europeText_inst, true))
				objects.removeValue(europeText_inst, true);
		}
		if (lookingAtExit) {
			objects.add(exitText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				levelChoice = 3;
			}
		} else {
			while (objects.contains(exitText_inst, true))
				objects.removeValue(exitText_inst, true);
		}
	}

	@Override
	public void loadAssets() {
		assets = new AssetManager();
		assets.load("ExitSign.g3db", Model.class);
		assets.load("ExitButton.g3db", Model.class);
		assets.load("GroundPlane.g3db", Model.class);
		assets.load("MenuFloor.g3db", Model.class);
		assets.load("SkyDome.g3db", Model.class);
		assets.load("Grass.g3db", Model.class);
		assets.load("BCText.g3db", Model.class);
		assets.load("RomeText.g3db", Model.class);
		assets.load("TreeHighPoly.g3db", Model.class);
		assets.load("RomanStatue.g3db", Model.class);
		assets.load("GunIcon.g3db", Model.class);
		assets.load("EuropeText.g3db", Model.class);
		assets.finishLoading();
	}

	/**
	 * Overridden loadModelInstances method. It loads all the model instanced of
	 * the scene and places them in the environment. It uses the
	 * {@link GameObject} class, a subclass of {@link ModelInstance} to make all
	 * the objects.
	 */
	@Override
	public void loadModelInstances() {
		// The locations of all the signs in clockwise order
		Vector3 BCLocation = new Vector3(0, 0, -5f);
		Vector3 europeLocation = new Vector3(-4.330f, 0, -2.5f);
		Vector3 romeLocation = new Vector3(4.330f, 0, -2.5f);
		Vector3 exitLocation = new Vector3(0f, 0, 5f);

		// The locations of all the text signs in clockwise order
		Vector3 BCTextLocation = new Vector3(0, 0, -4f);
		Vector3 romeTextLocation = new Vector3(3.464f, 0, -2f);
		Vector3 europeTextLocation = new Vector3(-3.464f, 0, -2f);
		Vector3 exitTextLocation = new Vector3(0, 0, 4f);

		Model rome = assets.get("RomeText.g3db", Model.class);
		romeText_inst = new GameObject(rome);
		romeText_inst.transform.translate(romeTextLocation).rotate(0, 1, 0, -60);

		Model BC = assets.get("BCText.g3db", Model.class);
		BCText_inst = new GameObject(BC);
		BCText_inst.transform.translate(BCTextLocation);

		Model europe = assets.get("EuropeText.g3db", Model.class);
		europeText_inst = new GameObject(europe);
		europeText_inst.transform.translate(europeTextLocation).rotate(0, 1, 0, 60);

		Model sky = assets.get("SkyDome.g3db", Model.class);
		ModelInstance sky_inst = new GameObject(sky);
		objects.add(sky_inst);

		for (int i = 0; i <= 360; i += 20) {
			Model grass = assets.get("Grass.g3db", Model.class);
			ModelInstance grass_inst = new GameObject(grass);
			grass_inst.transform.rotate(0, 1, 0, i);
			objects.add(grass_inst);
		}

		Model ground = assets.get("MenuFloor.g3db", Model.class);
		ModelInstance ground_inst = new GameObject(ground);
		objects.add(ground_inst);

		Model exit = assets.get("ExitSign.g3db", Model.class);
		ModelInstance exit_inst = new GameObject(exit);
		exit_inst.transform.translate(exitLocation).rotate(0, 1f, 0, 180);
		objects.add(exit_inst);

		Model exitText = assets.get("ExitButton.g3db", Model.class);
		exitText_inst = new GameObject(exitText);
		exitText_inst.transform.translate(exitTextLocation).rotate(0, 1f, 0, 180);

		Model tree = assets.get("TreeHighPoly.g3db", Model.class);
		ModelInstance tree_inst = new GameObject(tree);
		tree_inst.transform.translate(BCLocation).scale(0.5f, 0.5f, 0.5f);
		objects.add(tree_inst);

		Model gun = assets.get("GunIcon.g3db", Model.class);
		ModelInstance gun_inst = new GameObject(gun);
		gun_inst.transform.translate(europeLocation).rotate(0, 1, 0, 60);
		objects.add(gun_inst);

		Model statue = assets.get("RomanStatue.g3db", Model.class);
		ModelInstance statue_inst = new GameObject(statue);
		statue_inst.transform.translate(romeLocation).rotate(0, 1, 0, -60);
		objects.add(statue_inst);
	}

	@Override
	public void camSetup() {
		// Create a camera and point it to our model
		Gdx.input.setCursorCatched(true);
		float playerHeight = 1f;
		ProjectXploraGame.camera.position.set(0f, 0f, playerHeight);
		ProjectXploraGame.camera.lookAt(0f, 1f, playerHeight);
		ProjectXploraGame.camera.near = 0.1f;
		ProjectXploraGame.camera.far = 110f;
		ProjectXploraGame.camera.update();
		cameraController = new PlayerCameraController(ProjectXploraGame.camera, settings);
		cameraController.lockPosition();
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
	}

	@Override
	public void environmentSetup() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 2f, 2f, 2f, 1f));
		environment.add(new DirectionalLight().set(0.01f, 0.01f, 0.01f, 0.1f, 0.1f, -1f));
	}

}
