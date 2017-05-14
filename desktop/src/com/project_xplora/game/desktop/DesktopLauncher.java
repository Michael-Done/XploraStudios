/**
 *  XploraStudios
 *  Ms. Krasteva
 *  DesktopLauncher.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
package com.project_xplora.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.project_xplora.game.ProjectXploraGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		ProjectXploraGame game = new ProjectXploraGame();
		config.width = 0;
		config.height = 0;
		new LwjglApplication(game, config);
	}
}
