/**
 *  XploraStudios
 *  Ms. Krasteva
 *  ProjectXploraGame.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
package com.project_xplora.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeSet;

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
	TreeSet<PlayerData> highscores;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void create() {
		Bullet.init();
		// Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),
		// true);
		try {
			FileInputStream fileIn = new FileInputStream("C:/Users/Public/Documents/highscores.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			highscores = TreeSet.class.cast(in.readObject());
			in.close();
			fileIn.close();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (IOException e) {
			highscores = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(highscores == null ){
			highscores = new TreeSet<PlayerData>();
		}
		for(PlayerData i : highscores){
			System.out.println(i.getPlayerName());
		}
		camera = new PerspectiveCamera();
		camera.fieldOfView = 60;
		currentScene = Level.STARTUP;
		settings = new Settings();
		settings.setMouseSens(10);
		scenes = new ObjectMap<Level, GameObjectController>();

		// scenes.put(Level.LEVEL_SELECT, new LevelSelect(settings));
		scenes.put(Level.MENU, new MenuScene(settings));
		scenes.put(Level.LEVEL_SELECT, new LevelSelect(settings));
		scenes.put(Level.BC, new BritishColombiaScene(settings));
		scenes.put(Level.SETTINGS, new SettingsScene(settings));
		// For testing purposes
		currentScene = Level.BC;
		// Get screen dimensions
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		System.out.println ("(create - width = " + screenWidth + " height = " + screenHeight);

		// Create ModelBatch that will render all models using a camera
		modelBatch = new ModelBatch();
	}

	@Override
	public void dispose() {
		try {
			FileOutputStream fileOut = new FileOutputStream("C:/Users/Public/Documents/highscores.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(highscores);
			out.close();
			fileOut.close();
			System.out.println("file saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
					System.out.println("Settings reset!");
					settings = ((SettingsScene) scenes.get(currentScene)).getNewSettings();
				}
				((SettingsScene) scenes.get(currentScene)).resetChoice();
				// for (Level i : Level.values()) {
				// if (scenes.get(i) != null) {
				// scenes.get(i).updateSettings(settings);
				// }
				// }
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
