package com.project_xplora.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ProjectXploraGame implements ApplicationListener {
	ModelBatch modelBatch;
	Environment environment;
	PerspectiveCamera camera;
	AssetManager assets;
	FirstPersonCameraController cameraController;

	int screenWidth;
	int screenHeight;

	Array<ModelInstance> instances = new Array<ModelInstance>();

	@Override
	public void create() {
		// Get screen dimensions
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		// Create ModelBatch that will render all models using a camera
		modelBatch = new ModelBatch();

		// Create a camera and point it to our model
		camera = new PerspectiveCamera(70, screenWidth, screenHeight);
		camera.position.set(0f, -30f, 10f);
		camera.lookAt(0, 0, 10);
		camera.near = 0.1f;
		camera.far = 300f;
		camera.update();

		// Create the generic camera input controller to make the app
		// interactive
		cameraController = new FirstPersonCameraController(camera);
		Gdx.input.setInputProcessor(cameraController);

		// Create an asset manager that lets us dispose all assets at once
		assets = new AssetManager();
		assets.load("RomanTemple.g3db", Model.class);
		assets.load("RomanStatue.g3db", Model.class);
		assets.finishLoading();

		// Create an instance of our crate model and put it in an array
		Model model = assets.get("RomanTemple.g3db", Model.class);
		ModelInstance inst = new ModelInstance(model);
		instances.add(inst);
		Model model2 = assets.get("RomanStatue.g3db", Model.class);
		ModelInstance inst2 = new ModelInstance(model2, new Vector3(6f, -11f, 3f));
		inst2.transform.rotate(1, 0, 0, 90);
		inst2.transform.scale(0.35f, 0.35f, 0.35f);
		instances.add(inst2);
		ModelInstance inst3 = new ModelInstance(model2, new Vector3(-6f, -11f, 3f));
		inst3.transform.rotate(1, 0, 0, 90);
		inst3.transform.scale(0.35f, 0.35f, 0.35f);
		instances.add(inst3);
		// Set up environment with simple lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(1f, 1f, 1f, -0.8f, 0.3f, -1f));
	}

	@Override
	public void dispose() {
		// Release all resources
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}

	@Override
	public void render() {
		// Respond to user events and update the camera
		cameraController.update();
		//instances.get(1).transform.translate(0.5f, 0.5f, 0.5f);
		instances.get(1).transform.rotate(0, 1f, 0f, 1);
		instances.get(2).transform.rotate(0, 1f, 0f, -1);
		// Clear the viewport
		Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Draw all model instances using the camera
		modelBatch.begin(camera);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// Update screen dimensions
		screenWidth = width;
		screenHeight = height;

		// Update viewport size and refresh camera matrices
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update(true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
