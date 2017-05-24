/**
 *  XploraStudios
 *  Ms. Krasteva
 *  DesktopLauncher.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
package com.project_xplora.game.desktop;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.project_xplora.game.ProjectXploraGame;

public class DesktopLauncher implements ActionListener {
	static class Resolution extends Point{
		public Resolution(int x, int y){
			super(x, y);
		}
		@Override
		public String toString(){
			return "" + (int)this.getX() + " x " + (int)this.getY();
		}
	}
	static Resolution[] resolutions = { new Resolution(320, 240), new Resolution(640, 480), new Resolution(800, 600), new Resolution(1024, 600),
			new Resolution(1024, 768), new Resolution(1152, 864), new Resolution(1280, 720), new Resolution(1280, 768),
			new Resolution(1280, 800), new Resolution(1280, 1024), new Resolution(1360, 768), new Resolution(1366, 768),
			new Resolution(1440, 900), new Resolution(1600, 900), new Resolution(1600, 1200), new Resolution(1680, 1050),
			new Resolution(1920, 1080), new Resolution(1920, 1200), new Resolution(2560, 1440), new Resolution(2560, 1600),
			new Resolution(3840, 2160) };
	static String[] onOrOff = {"ON", "OFF"};
	
	static JComboBox<Resolution> resolutionSelection = new JComboBox<Resolution>(resolutions);
	static JComboBox<String> vsyncSelection = new JComboBox<String>(onOrOff);
	static JComboBox<String> fullscreenSelection = new JComboBox<String>(onOrOff);

	static JButton start = new JButton("Start");

	static int height = 1080;
	static int width = 1920;
	static boolean vsync = true;
	static boolean fullscreen = true;

	static boolean pass = false;

	public static void main(String[] arg) {
		JFrame setup = new JFrame();
		setup.setLocationRelativeTo(null);
		setup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup.setLayout(new FlowLayout());
		setup.add(new JLabel("Resolution: "));
		resolutionSelection.setSelectedIndex(16);
		setup.add(resolutionSelection);
		setup.add(new JLabel("Vsync: "));
		setup.add(vsyncSelection);
		setup.add(new JLabel("Fullscreen: "));
		setup.add(fullscreenSelection);
		start.addActionListener(new DesktopLauncher());
		setup.add(start);
		setup.pack();
		setup.setSize(200, 300);
		setup.setVisible(true);
		while(!pass){
			System.out.println(pass);
			System.out.println(height + " | " + width);
		}
		setup.dispose();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		ProjectXploraGame game = new ProjectXploraGame();
		config.title = "ProjectXplora 1.0";
		config.width = width;
		config.height = height;
		config.fullscreen = fullscreen;
		config.vSyncEnabled = vsync;
		new LwjglApplication(game, config);
	}

	public void actionPerformed(ActionEvent e) {
		width = (int)(((Resolution) (resolutionSelection.getSelectedItem())).getX());
		height = (int)(((Resolution) (resolutionSelection.getSelectedItem())).getY());
		fullscreen = fullscreenSelection.getSelectedItem().equals("ON");
		vsync = vsyncSelection.getSelectedItem().equals("ON");
		pass = true;
	}
}
