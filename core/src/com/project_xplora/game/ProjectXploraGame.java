/**
 *  XploraStudios
 *  Ms. Krasteva
 *  ProjectXploraGame.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
package com.project_xplora.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.project_xplora.game.highscore.PlayerData;

public class ProjectXploraGame implements ApplicationListener {
	static PerspectiveCamera camera;
	ModelBatch modelBatch;
	ObjectMap<Level, GameObjectController> scenes;
	Settings settings;
	public ArrayList<PlayerData> players;

	public enum Level {
		MENU, LEVEL_SELECT, EXIT, SETTINGS, ARTIFACT, HIGHSCORES, INSTRUCTION, ROME, EUROPE, BC, STARTUP
	}

	Level currentScene;
	public int screenWidth;
	public int screenHeight;

	Array<ModelInstance> instances = new Array<ModelInstance>();

	@Override
	public void create() {
		Bullet.init();
		// Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),
		// true);
		camera = new PerspectiveCamera();
		currentScene = Level.STARTUP;
		settings = new Settings();
		settings.setMouseSens(10);
		scenes = new ObjectMap<Level, GameObjectController>();

		// scenes.put(Level.LEVEL_SELECT, new LevelSelect(settings));
		scenes.put(Level.MENU, new MenuScene(settings));
		scenes.put(Level.LEVEL_SELECT, new LevelSelect(settings));
		scenes.put(Level.SETTINGS, new SettingsScene(settings));
		// For testing purposes
		currentScene = Level.MENU;
		// Get screen dimensions
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		System.out.println("(create - width = " + screenWidth + " height = " + screenHeight);
		// Create ModelBatch that will render all models using a camera
		modelBatch = new ModelBatch();
	}

	@Override
	public void dispose() {

		// Release all resources
		modelBatch.dispose();
		instances.clear();

	}

	@Override
	public void render() {
		// Clear the viewport
		Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		// Update current scene
		scenes.get(currentScene).update();
		if (currentScene == Level.SETTINGS) {
			Gdx.gl.glClearColor(0.449f, 0.645f, 0.739f, 1);
		} else {
			// Draw all model instances using the camera
			modelBatch.begin(camera);
			modelBatch.render(scenes.get(currentScene).objects, scenes.get(currentScene).getEnvironment());
			modelBatch.end();
		}
		// System.out.println(currentScene);
		// Check for scene changes, especially relating to the menu
		switch (currentScene) {
		case MENU:
			if (((MenuScene) scenes.get(currentScene)).getChoice() == 3) {
				currentScene = Level.EXIT;
				Gdx.app.exit();
			} else if (((MenuScene) scenes.get(currentScene)).getChoice() == 0) {
				((MenuScene) scenes.get(currentScene)).resetMenuChoice();
				currentScene = Level.LEVEL_SELECT;
			} else if (((MenuScene) scenes.get(currentScene)).getChoice() == 5) {
				((MenuScene) scenes.get(currentScene)).resetMenuChoice();
				currentScene = Level.SETTINGS;
				scenes.get(currentScene).loadModelInstances();
				((SettingsScene) scenes.get(currentScene)).setInputProccessor();
			}
			break;
		case LEVEL_SELECT:
			if (((LevelSelect) scenes.get(currentScene)).getLevelChoice() == 3) {
				((LevelSelect) scenes.get(currentScene)).resetLevelChoice();
				currentScene = Level.MENU;
				camera.lookAt(0f, 1f, 1f);
				// scenes.get(currentScene).initalize();
			}
			break;
		case SETTINGS:
			if (((SettingsScene) scenes.get(currentScene)).getChoice() != 0) {
				if (((SettingsScene) scenes.get(currentScene)).getChoice() == 1) {
					settings = ((SettingsScene) scenes.get(currentScene)).getNewSettings();
				}
				((SettingsScene) scenes.get(currentScene)).resetChoice();
//				for (Level i : Level.values()) {
//					if (scenes.get(i) != null) {
//						scenes.get(i).updateSettings(settings);
//					}
//				}
				currentScene = Level.MENU;
				scenes.get(currentScene).updateSettings(settings);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
			}
		default:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {
		// Update screen dimensions
		screenWidth = width;
		screenHeight = height;
		// Update viewport size and refresh camera matrices
		scenes.get(currentScene).cameraResize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
