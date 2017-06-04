/**
 *  XploraStudios
 *  Ms. Krasteva
 *  GameObjectcontroller.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Array;

public class GameObjectController {
	/** The list of objects to be rendered */
	Array<ModelInstance> objects;
	/** The model instance of the player */
	ModelInstance player;
	/** The environment settings for the scene*/
	Environment environment;
	/** The camera controller that controls the camera */
	public PlayerCameraController cameraController;
	/** The assets manager used to load all assets */
	AssetManager assets;
	/** The list of user settings */
	Settings settings;
	/** the width of the screen */
	public int screenWidth;
	/** The height of the screen */
	public int screenHeight;
	/** true if the game is paused */
	public boolean paused = false;
	/** Class constructor used to initalize settings and objects */
	public GameObjectController(Settings settings) {
		objects = new Array<ModelInstance>();
		this.settings = settings;
	}
	/** Loads all of the assets into the asset manager */
	public void loadAssets() {
		// Create an asset manager that lets us dispose all assets at once
		assets = new AssetManager();
		assets.load("GroundPlane.g3db", Model.class);
		assets.load("RomanTemple.g3db", Model.class);
		assets.load("RomanStatue.g3db", Model.class);
		assets.load("Icosphere.g3db", Model.class);
		assets.load("Tank.g3db", Model.class);
		assets.load("Mountains2.g3db", Model.class);
		assets.finishLoading();
	}
	/** locks the position of the camera */
	public void lockCamPosition() {
		cameraController.lockPosition();
	}
	/** unlocks the position of the camera */
	public void unlockCamPosition() {
		cameraController.unlockPosition();
	}
	/** Updates the settings and controls */
	public void updateSettings(Settings settings){
		cameraController.updateSettings(settings);
	}
	/** loads all model instances into objects */
	public void loadModelInstances() {
		// Create an instance of our crate model and put it in an array

		Model ground = assets.get("GroundPlane.g3db", Model.class);
		ModelInstance ground_inst = new ModelInstance(ground);
		ground_inst.transform.rotate(1, 0, 0, 90);
		objects.add(ground_inst);
		/*
		 * Model icosphere = assets.get("Icosphere.g3db", Model.class);
		 * ModelInstance icosphere_inst = new ModelInstance(icosphere);
		 * icosphere_inst.transform.translate(-10, -10, 15);
		 * instances.add(icosphere_inst);
		 */
		Model temple = assets.get("RomanTemple.g3db", Model.class);
		ModelInstance temple_inst = new ModelInstance(temple);
		objects.add(temple_inst);
		/*
		 * Model tank = assets.get("Tank.g3db", Model.class); ModelInstance
		 * tank_inst = new ModelInstance(tank);
		 * //tank_inst.transform.scale(0.5f, 0.5f, 0.5f);
		 * tank_inst.transform.translate(30f, 30f, 0f);
		 * tank_inst.transform.rotate(0, 0, 1, 90);
		 * tank_inst.transform.rotate(0, 1, 0, 180); instances.add(tank_inst);
		 */
		Model statue_1 = assets.get("RomanStatue.g3db", Model.class);

		ModelInstance statue_1_inst = new ModelInstance(statue_1, new Vector3(6f, -11f, 3f));
		statue_1_inst.transform.rotate(1, 0, 0, 90);
		statue_1_inst.transform.scale(0.35f, 0.35f, 0.35f);
		objects.add(statue_1_inst);

		ModelInstance statue_2_inst = new ModelInstance(statue_1, new Vector3(-6f, -11f, 3f));
		statue_2_inst.transform.rotate(1, 0, 0, 90);
		statue_2_inst.transform.scale(0.35f, 0.35f, 0.35f);
		objects.add(statue_2_inst);
		/*
		 * ballShape = new btSphereShape(1.75f); groundShape = new
		 * btBoxShape(new Vector3(50f, 2.5f, 50f));
		 * 
		 * groundObject = new btCollisionObject();
		 * groundObject.setCollisionShape(groundShape);
		 * groundObject.setWorldTransform(ground_inst.transform);
		 * 
		 * ballObject = new btCollisionObject();
		 * ballObject.setCollisionShape(ballShape);
		 * ballObject.setWorldTransform(icosphere_inst.transform);
		 */
	}
	/** sets up the camera */
	public void camSetup() {
		// Create a camera and point it to our model
		Gdx.input.setCursorCatched(true);
		//camera = new PerspectiveCamera(70, screenWidth, screenHeight);
		float playerHeight = 3f;
		ProjectXploraGame.camera.position.set(0f, -30f, playerHeight);
		ProjectXploraGame.camera.lookAt(0f, 0f, playerHeight);
		ProjectXploraGame.camera.near = 0.1f;
		ProjectXploraGame.camera.far = 3000f;
		ProjectXploraGame.camera.update();
		cameraController = new PlayerCameraController(ProjectXploraGame.camera, settings);
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
	}
	/** sets up the environment */
	public void environmentSetup() {
		// Set up environment with simple lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 2f, 2f, 2f, 1f));
		for (float i = 0; i < 360; i++) {
			environment.add(new DirectionalLight().set(0.1f, 0.1f, 0.1f, -0.8f, i, -1f));
		}
	}
	/** initializes the entire scene */
	public void initalize() {
		camSetup();
		loadAssets();
		loadModelInstances();
		environmentSetup();
	}
	/** disposes of all resources */
	public void disposeAll() {
		assets.dispose();
		objects.clear();
	}
	/** updates the camera */
	public void updateCamera() {
		cameraController.update();
	}

	/** returns the environment
	 * @return environment
	 */
	public Environment getEnvironment() {
		return environment;
	}
	/** Resizes the camera */
	public void cameraResize(int width, int height) {
		ProjectXploraGame.camera.viewportWidth = width;
		ProjectXploraGame.camera.viewportHeight = height;
		ProjectXploraGame.camera.update(true);
	}
	/** updates all aspects of the scene */
	public void update() {
		updateCamera();
		if(cameraController != null && cameraController.paused){
			pause();
		} 
	}
	public void pause(){
		paused = true;
	}
	public void unPause(){
		paused = false;
		if(cameraController != null){
			cameraController.unPause();
		}
	}
	
}
