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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.utils.Array;

public class GameObjectController {
	Array<ModelInstance> objects;
	ModelInstance player;

	Environment environment;

	PerspectiveCamera camera;
	PlayerCameraController cameraController;

	AssetManager assets;

	int screenWidth;
	int screenHeight;

	public GameObjectController() {
		objects = new Array<ModelInstance>();

	}

	public void initalize() {
		// Create a camera and point it to our model
		Gdx.input.setCursorCatched(true);
		camera = new PerspectiveCamera(70, screenWidth, screenHeight);
		camera.position.set(0f, -30f, 10f);
		camera.lookAt(0, 0, 10);
		camera.near = 0.1f;
		camera.far = 3000f;
		camera.update();

		cameraController = new PlayerCameraController(camera);
		Gdx.input.setInputProcessor(cameraController);

		// Create an asset manager that lets us dispose all assets at once
		assets = new AssetManager();
		assets.load("GroundPlane.g3db", Model.class);
		assets.load("RomanTemple.g3db", Model.class);
		assets.load("RomanStatue.g3db", Model.class);
		assets.load("Icosphere.g3db", Model.class);
		assets.load("Tank.g3db", Model.class);
		assets.load("Mountains2.g3db", Model.class);
		assets.finishLoading();

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
		 * 
		 * Model statue_1 = assets.get("RomanStatue.g3db", Model.class);
		 * 
		 * ModelInstance statue_1_inst = new ModelInstance(statue_1, new
		 * Vector3(6f, -11f, 3f)); statue_1_inst.transform.rotate(1, 0, 0, 90);
		 * statue_1_inst.transform.scale(0.35f, 0.35f, 0.35f);
		 * instances.add(statue_1_inst);
		 * 
		 * ModelInstance statue_2_inst = new ModelInstance(statue_1, new
		 * Vector3(-6f, -11f, 3f)); statue_2_inst.transform.rotate(1, 0, 0, 90);
		 * statue_2_inst.transform.scale(0.35f, 0.35f, 0.35f);
		 * instances.add(statue_2_inst);
		 * 
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

		// Set up environment with simple lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 2f, 2f, 2f, 1f));
		for (float i = 0; i < 360; i++) {
			environment.add(new DirectionalLight().set(0.1f, 0.1f, 0.1f, -0.8f, i, -1f));
		}
		
		cameraResize(screenWidth, screenHeight);
	}

	public void disposeAll() {
		assets.dispose();
		objects.clear();
	}
	
	public void update(){
		cameraController.update();
	}
	public Camera getCamera(){
		return camera;
	}
	public Environment getEnvironment(){
		return environment;
	}
	public void cameraResize(int width, int height){
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update(true);
	}
}
