package com.project_xplora.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.project_xplora.game.highscore.PlayerData;
import com.project_xplora.game.PauseMenu.PauseStage;
import com.project_xplora.game.educational.minigames.AncientRomeQuiz;
import com.project_xplora.game.educational.minigames.BritishColumbiaQuiz;
import com.project_xplora.game.educational.minigames.EducationalQuiz;
import com.project_xplora.game.educational.minigames.WorldWar2Quiz;

/**
 * The ProjectXploraGame class. This is where all program control and flow is
 * processed. Each frame the game goes through the render method.
 * <p>
 * Time taken to complete: 7 hours
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class ProjectXploraGame implements ApplicationListener {
	/** the highscores */
	static TreeSet<PlayerData> highscores;
	/** the camera used throughout the game */
	static PerspectiveCamera camera;
	/** the model batch used for all objects */
	ModelBatch modelBatch;
	/** the list of scenes */
	ObjectMap<Level, GameObjectController> scenes;
	/** the settings */
	Settings settings;
	/** the current player's data */
	static PlayerData currentPlayer;
	/** if the game is paused */
	public boolean paused = false;
	/** the timer used to score the player */
	public static Timer timer;
	/** the music player */
	private Music musicPlayer;
	/** the list of music files */
	private String[] musicFileNames;
	/** whether or not to play music */
	private boolean callMusicPlay;

	/** the list of levels */
	public enum Level {
		SPLASHSCREEN, MENU, LEVEL_SELECT, EXIT, SETTINGS, ARTIFACT, HIGHSCORES, INSTRUCTION, ROME, EUROPE, BC, STARTUP, CREDITS, MINIGAME1, MINIGAME2, MINIGAME3, NAMESELECT, GAMEFINISH
	}

	/** the pause menu */
	PauseMenu pauseMenu;
	/** the current scene */
	Level currentScene;
	/** the screen width */
	public int screenWidth;
	/** the screen height */
	public int screenHeight;
	/** the list of all models being rendered */
	Array<ModelInstance> instances = new Array<ModelInstance>();

	/**
	 * initalizes EVERYTHING
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void create() {
		Bullet.init();
		pauseMenu = new PauseMenu();

		// Initialize Music
		musicFileNames = new String[5];
		musicFileNames[0] = "SplashScreen.mp3";
		musicFileNames[1] = "MenuAndScreens.mp3";
		musicFileNames[2] = "RomeTrack3.mp3";
		musicFileNames[3] = "EuropeTrack1.mp3";
		musicFileNames[4] = "BCTrack1.mp3";
		musicPlayer = Gdx.audio.newMusic(Gdx.files.internal(musicFileNames[0]));

		try {
			FileInputStream fileIn = new FileInputStream(System.getProperty("user.home") + "/highscores.ser");
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
		if (highscores == null) {
			highscores = new TreeSet<PlayerData>();
		}
		for (PlayerData i : highscores) {
			System.out.println("something here");
		}
		camera = new PerspectiveCamera();
		camera.fieldOfView = 60;
		currentScene = Level.STARTUP;
		settings = new Settings();
		settings.setMouseSens(10);
		scenes = new ObjectMap<Level, GameObjectController>();

		scenes.put(Level.SPLASHSCREEN, new SplashScreen(settings));
		scenes.put(Level.INSTRUCTION, new InstructionsScene(settings));
		scenes.put(Level.ARTIFACT, new ArtifactScene(settings));
		scenes.put(Level.LEVEL_SELECT, new LevelSelect(settings));
		scenes.put(Level.BC, new BritishColombiaScene(settings));
		scenes.put(Level.SETTINGS, new SettingsScene(settings));
		scenes.put(Level.MENU, new MenuScene(settings));
		scenes.put(Level.CREDITS, new CreditsScene(settings));
		scenes.put(Level.ROME, new RomeScene(settings));
		scenes.put(Level.EUROPE, new EuropeScene(settings));
		scenes.put(Level.MINIGAME1, new BritishColumbiaQuiz(settings));
		scenes.put(Level.MINIGAME2, new WorldWar2Quiz(settings));
		scenes.put(Level.MINIGAME3, new AncientRomeQuiz(settings));
		scenes.put(Level.NAMESELECT, new NameSelect(settings));
		scenes.put(Level.HIGHSCORES, new HighScoresScene(settings, highscores));

		currentScene = Level.SPLASHSCREEN;

		scenes.get(currentScene).camSetup();
		// Get screen dimensions
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		// Create ModelBatch that will render all models using a camera
		modelBatch = new ModelBatch();
	}

	/**
	 * called when the app exits. Saves the highscores
	 */
	@Override
	public void dispose() {
		try {
			if (timer.player.getTime() != 0)
				highscores.add(timer.player);
			FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "/highscores.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(highscores);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Release all resources
		modelBatch.dispose();
		instances.clear();

	}

	/** called each frame. Controls all program flow as well. */
	@Override
	public void render() {

		// Clear the viewport
		Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		// Update current scene
		scenes.get(currentScene).update();
		if (currentScene == Level.SETTINGS) {
			Gdx.gl.glClearColor(0.449f, 0.645f, 0.739f, 1f);
		} else {
			// Draw all model instances using the camera
			modelBatch.begin(camera);
			modelBatch.render(scenes.get(currentScene).objects, scenes.get(currentScene).getEnvironment());
			modelBatch.end();
		}
		// System.out.println(currentScene);
		// Check for scene changes, especially relating to the menu
		System.out.println(currentScene);
		switch (currentScene) {
		case SPLASHSCREEN:
			musicPlayer.play();
			if (Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
				currentScene = Level.NAMESELECT;
				scenes.get(currentScene).camSetup();
			}
			break;
		case NAMESELECT:
			if (((NameSelect) scenes.get(currentScene)).cont) {
				currentPlayer = new PlayerData(((NameSelect) scenes.get(currentScene)).name);
				timer = new Timer(currentPlayer);
				currentScene = Level.MENU;
				scenes.put(Level.GAMEFINISH, new GameCompletedScene(settings));
				scenes.get(currentScene).updateSettings(settings);
				camera.position.set(0, 0, 1);
				camera.lookAt(0f, 1f, 1f);
				musicPlayer.stop();
				musicPlayer.dispose();
				callMusicPlay = true;
				Gdx.input.setCursorCatched(true);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
			}
			break;
		case MENU:
			if (callMusicPlay) {
				musicPlayer = Gdx.audio.newMusic(Gdx.files.internal(musicFileNames[1]));
				musicPlayer.play();
				musicPlayer.setLooping(true);
				callMusicPlay = false;
			}
			if (((MenuScene) scenes.get(currentScene)).getChoice() == 3) {
				currentScene = Level.CREDITS;
				musicPlayer.stop();
			} else if (((MenuScene) scenes.get(currentScene)).getChoice() == 0) {
				((MenuScene) scenes.get(currentScene)).resetMenuChoice();
				currentScene = Level.LEVEL_SELECT;
				scenes.get(currentScene).updateSettings(settings);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (((MenuScene) scenes.get(currentScene)).getChoice() == 1) {
				((MenuScene) scenes.get(currentScene)).resetMenuChoice();
				currentScene = Level.INSTRUCTION;
			} else if (((MenuScene) scenes.get(currentScene)).getChoice() == 2) {
				((MenuScene) scenes.get(currentScene)).resetMenuChoice();
				currentScene = Level.ARTIFACT;
			} else if (((MenuScene) scenes.get(currentScene)).getChoice() == 5) {
				((MenuScene) scenes.get(currentScene)).resetMenuChoice();
				currentScene = Level.SETTINGS;
				scenes.get(currentScene).updateSettings(settings);
				scenes.get(currentScene).loadModelInstances();
				((SettingsScene) scenes.get(currentScene)).setInputProccessor();
			} else if (((MenuScene) scenes.get(currentScene)).getChoice() == 4) {
				((MenuScene) scenes.get(currentScene)).resetMenuChoice();
				currentScene = Level.HIGHSCORES;
				scenes.get(currentScene).camSetup();
			}
			break;
		case INSTRUCTION:
			if (Gdx.input.isKeyJustPressed(Keys.C)) {
				((InstructionsScene) scenes.get(currentScene)).incrementScreen();
				((InstructionsScene) scenes.get(currentScene)).changeCurrentTexture();
			} else if (Gdx.input.isKeyJustPressed(Keys.X)) {
				((InstructionsScene) scenes.get(currentScene)).resetMenuScreen();
				currentScene = Level.MENU;
				scenes.get(currentScene).updateSettings(settings);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
			}
			break;
		case ARTIFACT:
			if (Gdx.input.isKeyJustPressed(Keys.C)) {
				((ArtifactScene) scenes.get(currentScene)).incrementScreen();
				((ArtifactScene) scenes.get(currentScene)).changeCurrentTexture();
			} else if (Gdx.input.isKeyJustPressed(Keys.X)) {
				((ArtifactScene) scenes.get(currentScene)).resetMenuScreen();
				currentScene = Level.MENU;
				scenes.get(currentScene).updateSettings(settings);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
			}
			break;
		case LEVEL_SELECT:
			if (((LevelSelect) scenes.get(currentScene)).getLevelChoice() == 3) {
				((LevelSelect) scenes.get(currentScene)).resetLevelChoice();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				currentScene = Level.MENU;
				scenes.get(currentScene).updateSettings(settings);
				camera.lookAt(0f, 1f, 1f);

			} else if (((LevelSelect) scenes.get(currentScene)).getLevelChoice() == 0) {
				musicPlayer.stop();
				musicPlayer.dispose();
				callMusicPlay = true;
				((LevelSelect) scenes.get(currentScene)).resetLevelChoice();
				currentScene = Level.ROME;
				scenes.get(currentScene).updateSettings(settings);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				camera.far = 3000f;
			} else if (((LevelSelect) scenes.get(currentScene)).getLevelChoice() == 2) {
				musicPlayer.stop();
				musicPlayer.dispose();
				callMusicPlay = true;
				((LevelSelect) scenes.get(currentScene)).resetLevelChoice();
				currentScene = Level.EUROPE;
				scenes.get(currentScene).updateSettings(settings);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				camera.far = 3000f;
			} else if (((LevelSelect) scenes.get(currentScene)).getLevelChoice() == 1) {
				musicPlayer.stop();
				musicPlayer.dispose();
				callMusicPlay = true;
				((LevelSelect) scenes.get(currentScene)).resetLevelChoice();
				currentScene = Level.BC;
				scenes.get(currentScene).updateSettings(settings);
				camera.position.set(149f, -50f, 1 + 12.36f);
				camera.far = 3000f;
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
			}
			break;
		case SETTINGS:
			if (((SettingsScene) scenes.get(currentScene)).getChoice() != 0) {
				if (((SettingsScene) scenes.get(currentScene)).getChoice() == 1) {
					System.out.println("Settings reset!");
					settings = ((SettingsScene) scenes.get(currentScene)).getNewSettings();
				}
				((SettingsScene) scenes.get(currentScene)).resetChoice();

				currentScene = Level.MENU;
				scenes.get(currentScene).updateSettings(settings);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
			}
			break;
		case GAMEFINISH:
			if (((GameCompletedScene) scenes.get(currentScene)).nextScene) {
				currentScene = Level.MENU;
				scenes.get(currentScene).updateSettings(settings);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
				camera.position.set(0, 0, 1);
				camera.lookAt(0f, 1f, 1f);
				musicPlayer.stop();
				musicPlayer.dispose();
				callMusicPlay = true;
			}
			break;
		case BC:
			if (callMusicPlay) {
				musicPlayer = Gdx.audio.newMusic(Gdx.files.internal(musicFileNames[4]));
				musicPlayer.play();
				musicPlayer.setLooping(true);
				callMusicPlay = false;
			}
			if (((BritishColombiaScene) scenes.get(currentScene)).isQuiz) {
				currentScene = Level.MINIGAME1;
				scenes.get(currentScene).camSetup();
			} else if (((BritishColombiaScene) scenes.get(currentScene)).moveToNext) {
				currentScene = Level.GAMEFINISH;
				timer.player.setBCCompleted(true);
				scenes.get(currentScene).camSetup();
				((GameCompletedScene) scenes.get(currentScene)).updateTable();
			} else if (((BritishColombiaScene) scenes.get(currentScene)).artifactsUnlocked >= 5) {
				((BritishColombiaScene) scenes.get(currentScene)).exitStage.draw();
			}
			timer.update(currentScene, paused);
			if (currentScene == Level.BC)
				((BritishColombiaScene) scenes.get(currentScene)).hud.draw();
			break;
		case MINIGAME1:
			if (((EducationalQuiz) scenes.get(currentScene)).isCorrect()) {
				((EducationalQuiz) scenes.get(currentScene)).resetExitMinigame();
				currentScene = Level.BC;
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
				((BritishColombiaScene) scenes.get(currentScene)).resetIsQuiz();
			}
			break;
		case EUROPE:
			if (callMusicPlay) {
				musicPlayer = Gdx.audio.newMusic(Gdx.files.internal(musicFileNames[3]));
				musicPlayer.play();
				musicPlayer.setLooping(true);
				callMusicPlay = false;
			}
			if (((EuropeScene) scenes.get(currentScene)).isQuiz) {
				currentScene = Level.MINIGAME2;
				scenes.get(currentScene).camSetup();
			} else if (((EuropeScene) scenes.get(currentScene)).moveToNext) {
				musicPlayer.stop();
				musicPlayer.dispose();
				callMusicPlay = true;
				currentScene = Level.BC;
				timer.player.setEuropecompleted(true);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
				ProjectXploraGame.camera.position.set(149f, -50f, 1 + 12.36f);
				ProjectXploraGame.camera.lookAt(149f, -49f, 1 + 12.36f);
				ProjectXploraGame.camera.near = 0.1f;
				ProjectXploraGame.camera.far = 1000f;
			} else if (((EuropeScene) scenes.get(currentScene)).artifactsUnlocked >= 5) {
				((EuropeScene) scenes.get(currentScene)).exitStage.draw();
			}
			timer.update(currentScene, paused);
			if (currentScene == Level.EUROPE)
				((EuropeScene) scenes.get(currentScene)).hud.draw();
			break;
		case MINIGAME2:
			if (((EducationalQuiz) scenes.get(currentScene)).isCorrect()) {
				((EducationalQuiz) scenes.get(currentScene)).resetExitMinigame();
				currentScene = Level.EUROPE;
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
				((EuropeScene) scenes.get(currentScene)).resetIsQuiz();
			}
			break;
		case ROME:
			if (callMusicPlay) {
				musicPlayer = Gdx.audio.newMusic(Gdx.files.internal(musicFileNames[2]));
				musicPlayer.play();
				musicPlayer.setLooping(true);
				callMusicPlay = false;
			}
			if (((RomeScene) scenes.get(currentScene)).isQuiz) {
				currentScene = Level.MINIGAME3;
				scenes.get(currentScene).camSetup();
			} else if (((RomeScene) scenes.get(currentScene)).moveToNext) {
				musicPlayer.stop();
				musicPlayer.dispose();
				callMusicPlay = true;
				currentScene = Level.EUROPE;
				timer.player.setRomeCompleted(true);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
				ProjectXploraGame.camera.position.set(0f, 0f, 1);
				ProjectXploraGame.camera.lookAt(0f, 1f, 1);
				ProjectXploraGame.camera.near = 0.1f;
				ProjectXploraGame.camera.far = 1000f;
				ProjectXploraGame.camera.update();
			} else if (((RomeScene) scenes.get(currentScene)).artifactsUnlocked >= 5) {
				((RomeScene) scenes.get(currentScene)).exitStage.draw();
			}
			timer.update(currentScene, paused);
			if (currentScene == Level.ROME)
				((RomeScene) scenes.get(currentScene)).hud.draw();
			break;
		case MINIGAME3:
			if (((EducationalQuiz) scenes.get(currentScene)).isCorrect()) {
				((EducationalQuiz) scenes.get(currentScene)).resetExitMinigame();
				currentScene = Level.ROME;
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				Gdx.input.setCursorCatched(true);
				((RomeScene) scenes.get(currentScene)).resetIsQuiz();
			}
			break;
		case HIGHSCORES:
			if (((HighScoresScene) scenes.get(currentScene)).backtoMenu) {
				((HighScoresScene) scenes.get(currentScene)).backtoMenu = false;
				currentScene = Level.MENU;
				scenes.get(currentScene).updateSettings(settings);
				camera.position.set(0, 0, 1);
				camera.lookAt(0f, 1f, 1f);
				Gdx.input.setCursorCatched(true);
				Gdx.input.setInputProcessor(scenes.get(currentScene).cameraController);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
		if (scenes.get(currentScene).paused && !paused) {
			paused = true;
			pauseMenu.setPrevious();
			Gdx.input.setCursorCatched(false);
			((PauseStage) pauseMenu.stage).paused = true;
			Gdx.input.setInputProcessor(pauseMenu.stage);
		} else if (!((PauseStage) pauseMenu.stage).paused && paused) {
			paused = false;
			pauseMenu.restorePrevious();
			scenes.get(currentScene).unPause();

		}
		if (paused) {
			pauseMenu.update();

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
