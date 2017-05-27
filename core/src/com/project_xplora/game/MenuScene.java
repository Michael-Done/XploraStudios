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

public class MenuScene extends GameObjectController {
	ModelInstance exitText_inst;
	ModelInstance playText_inst;
	ModelInstance settingsText_inst;
	ModelInstance highscoresText_inst;
	ModelInstance artifactText_inst;
	ModelInstance instructionText_inst;

	int menuChoice = -1;

	public MenuScene(Settings settings) {
		super(settings);
		initalize();
		// TODO Auto-generated constructor stub
	}

	public int getChoice() {
		return menuChoice;
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

		boolean lookingAtPlay = cameraController.getXYAngle() > -30 && cameraController.getXYAngle() < 30;
		boolean lookingAtInstruction = cameraController.getXYAngle() > 30 && cameraController.getXYAngle() < 90;
		boolean lookingAtArtifact = cameraController.getXYAngle() > 90 && cameraController.getXYAngle() < 150;
		boolean lookingAtExit = cameraController.getXYAngle() > 150 || cameraController.getXYAngle() < -150;
		boolean lookingAtHighscores = cameraController.getXYAngle() > -150 && cameraController.getXYAngle() < -90;
		boolean lookingAtSettings = cameraController.getXYAngle() > -90 && cameraController.getXYAngle() < -30;

		if (lookingAtPlay) {
			objects.add(playText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				menuChoice = 0;
			}
		} else {
			while (objects.contains(playText_inst, true))
				objects.removeValue(playText_inst, true);
		}

		if (lookingAtInstruction) {
			objects.add(instructionText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				menuChoice = 1;
			}
		} else {
			while (objects.contains(instructionText_inst, true))
				objects.removeValue(instructionText_inst, true);
		}

		if (lookingAtArtifact) {
			objects.add(artifactText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				menuChoice = 2;
			}
		} else {
			while (objects.contains(artifactText_inst, true))
				objects.removeValue(artifactText_inst, true);
		}

		if (lookingAtExit) {
			objects.add(exitText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				menuChoice = 3;
			}
		} else {
			while (objects.contains(exitText_inst, true))
				objects.removeValue(exitText_inst, true);
		}

		if (lookingAtHighscores) {
			objects.add(highscoresText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				menuChoice = 4;
			}
		} else {
			while (objects.contains(highscoresText_inst, true))
				objects.removeValue(highscoresText_inst, true);
		}

		if (lookingAtSettings) {
			objects.add(settingsText_inst);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				menuChoice = 5;
			}
		} else {
			while (objects.contains(settingsText_inst, true))
				objects.removeValue(settingsText_inst, true);
		}
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
		assets.load("HighscoresText.g3db", Model.class);
		assets.load("SettingsText.g3db", Model.class);
		assets.load("PlaySign.g3db", Model.class);
		assets.load("Trophy.g3db", Model.class);
		assets.load("Gear.g3db", Model.class);
		assets.load("Instructions.g3db", Model.class);
		assets.load("ArtefactText.g3db", Model.class);
		assets.load("Artefact.g3db", Model.class);
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
		// The locations of all the signs
		Vector3 playLocation = new Vector3(0, 0, -5f);
		Vector3 instructionLocation = new Vector3(4.330f, 0, -2.5f);
		Vector3 artifactLocation = new Vector3(4.330f, 0, 2.5f);
		Vector3 exitLocation = new Vector3(0, 0, 5f);
		Vector3 highscoresLocation = new Vector3(-4.330f, 0, 2.5f);
		Vector3 settingsLocation = new Vector3(-4.330f, 0, -2.5f);

		// The locations of all the text signs
		Vector3 playTextLocation = new Vector3(0, 0, -4f);
		Vector3 instructionTextLocation = new Vector3(3.464f, 0, -2f);
		Vector3 artifactTextLocation = new Vector3(3.464f, 0, 2f);
		Vector3 exitTextLocation = new Vector3(0, 0, 4f);
		Vector3 highscoresTextLocation = new Vector3(-3.464f, 0, 2f);
		Vector3 settingsTextLocation = new Vector3(-3.464f, 0, -2f);

		// Create an instance of our crate model and put it in an array
		Model exitText = assets.get("ExitButton.g3db", Model.class);
		exitText_inst = new GameObject(exitText);
		exitText_inst.transform.translate(exitTextLocation).rotate(0, 1f, 0, 180);

		Model playText = assets.get("PlayText.g3db", Model.class);
		playText_inst = new GameObject(playText);
		playText_inst.transform.translate(playTextLocation);

		Model settingsText = assets.get("SettingsText.g3db", Model.class);
		settingsText_inst = new GameObject(settingsText);
		settingsText_inst.transform.translate(settingsTextLocation).rotate(0, 1f, 0, 60);

		Model highscoresText = assets.get("HighscoresText.g3db", Model.class);
		highscoresText_inst = new GameObject(highscoresText);
		highscoresText_inst.transform.translate(highscoresTextLocation).rotate(0, 1f, 0, 120);

		Model artifactText = assets.get("ArtefactText.g3db", Model.class);
		artifactText_inst = new GameObject(artifactText);
		artifactText_inst.transform.translate(artifactTextLocation).rotate(0, 1f, 0, -120);

		Model instructionText = assets.get("InstructionText.g3db", Model.class);
		instructionText_inst = new GameObject(instructionText);
		instructionText_inst.transform.translate(instructionTextLocation).rotate(0, 1f, 0, -60);

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

		Model play = assets.get("PlaySign.g3db", Model.class);
		ModelInstance play_inst = new GameObject(play);
		play_inst.transform.translate(playLocation);
		objects.add(play_inst);
		
		Model highscores = assets.get("Trophy.g3db", Model.class);
		ModelInstance highscores_inst = new GameObject(highscores);
		highscores_inst.transform.translate(highscoresLocation).rotate(0, 1f, 0, 120f);
		objects.add(highscores_inst);
		
		Model settings = assets.get("Gear.g3db", Model.class);
		ModelInstance settings_inst = new GameObject(settings);
		settings_inst.transform.translate(settingsLocation).rotate(0, 1f, 0, 60f);
		objects.add(settings_inst);
		
		Model instructions = assets.get("Instructions.g3db", Model.class);
		ModelInstance instructions_inst = new GameObject(instructions);
		instructions_inst.transform.translate(instructionLocation).rotate(0, 1f, 0, -60f);
		objects.add(instructions_inst);
		
		Model artefact = assets.get("Artefact.g3db", Model.class);
		ModelInstance artefact_inst = new GameObject(artefact);
		artefact_inst.transform.translate(artifactLocation).rotate(0, 1f, 0, -60f);
		objects.add(artefact_inst);

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
		//cameraController.unlockPosition();
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
	}

	@Override
	public void environmentSetup() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 2f, 2f, 2f, 1f));
		environment.add(new DirectionalLight().set(0.01f, 0.01f, 0.01f, 0.1f, 0.1f, -1f));
	}
	public void resetMenuChoice(){
		menuChoice = -1;
	}
}
