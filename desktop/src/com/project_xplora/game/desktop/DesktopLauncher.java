/**
 *  XploraStudios
 *  Ms. Krasteva
 *  DesktopLauncher.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */

package com.project_xplora.game.desktop;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.project_xplora.game.ProjectXploraGame;

public class DesktopLauncher implements ActionListener {
	//=== Fields ===
	private static Resolution[] resolutions = { new Resolution(320, 240), new Resolution(640, 480),
			new Resolution(800, 600), new Resolution(1024, 600), new Resolution(1024, 768), new Resolution(1152, 864),
			new Resolution(1280, 720), new Resolution(1280, 768), new Resolution(1280, 800), new Resolution(1280, 1024),
			new Resolution(1360, 768), new Resolution(1366, 768), new Resolution(1440, 900), new Resolution(1600, 900),
			new Resolution(1600, 1200), new Resolution(1680, 1050), new Resolution(1920, 1080),
			new Resolution(1920, 1200), new Resolution(2560, 1440), new Resolution(2560, 1600),
			new Resolution(3840, 2160) };
	private static JFrame setup = new JFrame("Game Launcher - Initial Setup");
	private static int width = 1920;
	private static int height = 1080;
	private static boolean vsync = true;
	private static boolean fullscreen = false;
	private static JComboBox<Resolution> resolutionSelection = new JComboBox<Resolution>(resolutions);
	private static JComboBox<String> vSyncSelection = new JComboBox<String>(new String[] { "ON", "OFF" });
	private static JComboBox<String> fullscreenSelection = new JComboBox<String>(
			new String[] { "Fullscreen", "Windowed" });

	//=== Screen Resolution Class ===
	private static class Resolution extends Point {
		//Constructor
		public Resolution(int x, int y) {
			super(x, y);
		}

		//Overridden toString method
		public String toString() {
			return "" + (int) this.getX() + " x " + (int) this.getY();
		}
	}

	//=== Game Launcher Method Setup ===
	private static void gameLauncher() {
		//Local Variables
		JButton start = new JButton("Start Game");
		Font settingFonts = new Font("Agency FB", Font.PLAIN, 20);
		JLabel title = new JLabel("Project Xplorer - Inital Settings Menu: ");
		JLabel resoLabel = new JLabel("Resolution: ");
		JLabel vSyncLabel = new JLabel("VSync: ");
		JLabel fullScreenLabel = new JLabel("    Screen: ");

		//Frame Setup
		setup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup.setLayout(new FlowLayout());
		title.setFont(new Font("Agency FB", Font.BOLD, 32));
		setup.add(Box.createHorizontalStrut(5));
		setup.add(title);
		resoLabel.setFont(settingFonts);
		setup.add(Box.createVerticalStrut(60));
		setup.add(resoLabel);
		resolutionSelection.setSelectedIndex(16);
		setup.add(resolutionSelection);
		vSyncLabel.setFont(settingFonts);
		setup.add(Box.createHorizontalStrut(60));
		setup.add(vSyncLabel);
		setup.add(vSyncSelection);
		fullScreenLabel.setFont(settingFonts);
		setup.add(Box.createVerticalStrut(10));
		setup.add(Box.createHorizontalStrut(28));
		setup.add(fullScreenLabel);
		setup.add(fullscreenSelection);
		start.addActionListener(new DesktopLauncher());
		setup.add(Box.createHorizontalStrut(200));
		setup.add(Box.createVerticalStrut(90));
		setup.add(start);

		//Set Screen
		setup.pack();
		setup.setSize(450, 300);
		setup.setVisible(true);
		setup.setLocationRelativeTo(null);
	}

	//=== Action Performed Method ===
	public void actionPerformed(ActionEvent e) {
		//Assign Values to Fields
		width = (int) (((Resolution) (resolutionSelection.getSelectedItem())).getX());
		height = (int) (((Resolution) (resolutionSelection.getSelectedItem())).getY());
		fullscreen = fullscreenSelection.getSelectedItem().equals("Fullscreen");
		vsync = vSyncSelection.getSelectedItem().equals("ON");
		
		//Setup Game Application
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		ProjectXploraGame game = new ProjectXploraGame();
		config.title = "ProjectXplora 1.0";
		config.width = width;
		config.height = height;
		config.fullscreen = fullscreen;
		config.vSyncEnabled = vsync;
		config.x = -1;
		config.y = -1;
		new LwjglApplication(game, config);

		//Close Setup Menu
		setup.dispose();
	}

	//=== Main Method ===
	public static void main(String[] arg) {
		gameLauncher();
	}
}