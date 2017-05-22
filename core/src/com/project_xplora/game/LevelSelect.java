package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
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
		// System.out.println(cameraController.getXYAngle());
		// boolean lookingAtRome = cameraController.getXYAngle() > -30 &&
		// cameraController.getXYAngle() < 30;
		// boolean lookingAtBC = cameraController.getXYAngle() > 30 &&
		// cameraController.getXYAngle() < 90;
		// boolean lookingAtEurope = cameraController.getXYAngle() > -90 &&
		// cameraController.getXYAngle() < -30;
		//
		// if (lookingAtRome) {
		// objects.add(romeText_inst);
		// if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
		// levelChoice = 0;
		// }
		// } else {
		// while (objects.contains(romeText_inst, true))
		// objects.removeValue(romeText_inst, true);
		// }
		//
		// if (lookingAtBC) {
		// objects.add(BCText_inst);
		// if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
		// levelChoice = 1;
		// }
		// } else {
		// while (objects.contains(BCText_inst, true))
		// objects.removeValue(BCText_inst, true);
		// }
		//
		// if (lookingAtEurope) {
		// objects.add(europeText_inst);
		// if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
		// levelChoice = 2;
		// }
		// } else {
		// while (objects.contains(europeText_inst, true))
		// objects.removeValue(europeText_inst, true);
		// }
	}

	@Override
	public void loadAssets() {
		assets = new AssetManager();
		assets.load("ExitSign.g3db", Model.class);
		assets.load("ExitButton.g3db", Model.class);
		assets.load("GroundPlane.g3db", Model.class);
		assets.load("PlayText.g3db", Model.class);
		assets.load("MenuFloor.g3db", Model.class);
		assets.load("SkyDome.g3db", Model.class);
		assets.load("Grass.g3db", Model.class);
		assets.load("InstructionText.g3db", Model.class);
		assets.load("SettingsText.g3db", Model.class);
		assets.load("PlaySign.g3db", Model.class);
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
		Vector3 playLocation = new Vector3(0, 0, -5f);
		Vector3 instructionLocation = new Vector3(-4.330f, 0, -2.5f);
		Vector3 highscoresLocation = new Vector3(4.330f, 0, -2.5f);
		Vector3 exitLocation = new Vector3(0f, 0, 5f);

		// The locations of all the text signs in clockwise order
		Vector3 playTextLocation = new Vector3(0, 0, -4f);
		Vector3 instructionTextLocation = new Vector3(3.464f, 0, -2f);
		Vector3 settingsTextLocation = new Vector3(-3.464f, 0, -2f);
		Vector3 exitTextLocation = new Vector3(0, 0, 4f);
		//
		// // Create an instance of our crate model and put it in an array
		//
		// Model playText = assets.get("PlayText.g3db", Model.class);
		// Text_inst = new GameObject(playText);
		// playText_inst.transform.translate(playTextLocation);
		//
		// Model highscoresText = assets.get("ExitButton.g3db", Model.class);
		// highscoresText_inst = new GameObject(highscoresText);
		// highscoresText_inst.transform.translate(highscoresTextLocation).rotate(0,
		// 1f, 0, 120);
		//
		// Model instructionText = assets.get("InstructionText.g3db",
		// Model.class);
		// instructionText_inst = new GameObject(instructionText);
		// instructionText_inst.transform.translate(instructionTextLocation).rotate(0,
		// 1f, 0, -60);
		//
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
		//
		// Model exit = assets.get("ExitSign.g3db", Model.class);
		// ModelInstance exit_inst = new GameObject(exit);
		// exit_inst.transform.translate(exitLocation).rotate(0, 1f, 0, 180);
		// objects.add(exit_inst);
		//
		// Model play = assets.get("PlaySign.g3db", Model.class);
		// ModelInstance play_inst = new GameObject(play);
		// play_inst.transform.translate(playLocation);
		// objects.add(play_inst);
		Model exit = assets.get("ExitSign.g3db", Model.class);
		ModelInstance exit_inst = new GameObject(exit);
		exit_inst.transform.translate(exitLocation).rotate(0, 1f, 0, 180);
		objects.add(exit_inst);

		// Create an instance of our crate model and put it in an array
		Model exitText = assets.get("ExitButton.g3db", Model.class);
		exitText_inst = new GameObject(exitText);
		exitText_inst.transform.translate(exitTextLocation).rotate(0, 1f, 0, 180);
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
		// cameraController.lockPosition();
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
