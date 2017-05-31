/*
 * [Authors]: Cyrus Gandevia and Michael Done.
 * [Date]: Friday, May 12th, 2017.
 * [Program Name]: DesktopLauncher.java
 * [Description]: The DesktopLauncher application is the Project Xplorer game's launcher. The purpose of 
 * this application is to set the preliminary settings of the game such as the game client's resolution,
 * whether the screen is full-screen or windowed, and whether the user would like to have VSync 
 * enabled or not, before the game begins. The launcher is displayed on a small, centered JFrame and has 
 * drop-down boxes for the user to select their choices. Once they are ready to begin the game, the user can
 * press the "Start Game" button which will then create the game client according to their chosen settings.
 * The JFrame disposes before the game begins.
 * */

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

/**
 * The DesktopLauncher application is the Project Xplorer game launcher. The
 * purpose of this application is to set the preliminary settings of the game
 * such as the game client's resolution, whether the screen is full-screen or
 * windowed, and whether the user would like to have VSync enabled or not,
 * before the game begins. The launcher is displayed on a small, centered JFrame
 * and has drop-down boxes for the user to select their choices. Once they are
 * ready to begin the game, the user can press the "Start Game" button which
 * will then create the game client according to their chosen settings. The
 * JFrame disposes before the game begins.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * Resolution [ ] <b> resolutions </b> - Stores the different resolution options
 * that the game launcher will have.
 * <p>
 * JFrame <b> setup </b> - Stores the instance of the JFrame that is being used
 * to display the desktop launcher.
 * <p>
 * int <b> width </b> - Stores the user's desired width for the game client
 * window.
 * <p>
 * int <b> height </b> - Stores the user's desired height for the game client
 * window.
 * <p>
 * boolean <b> vsync </b> - Stores the user's choice for the VSync setting.
 * <p>
 * boolean <b> fullscreen </b> - Stores the user's choice for the fullscreen
 * setting.
 * <p>
 * JComboBox &lt;Resolution&gt; <b> resolutionSelection </b> - Creates a
 * drop-down list for the resolution options.
 * <p>
 * JComboBox &lt;String&gt; <b> vSyncSelection </b> - Creates a drop-down list
 * for the VSync options.
 * <p>
 * JComboBox &lt;String&gt; <b> fullscreenSelection </b> - Creates a drop-down
 * list for the screen mode options.
 * <p>
 * <h2>Course Info:</h2> ICS4U0 with Krasteva, V.
 * <p>
 * 
 * @version 1.0 | 12.05.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */

public class DesktopLauncher implements ActionListener {
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

	/**
	 * The Resolution class is a custom-made helper class that is used for
	 * storing resolution options. It extends the Point class, which is a
	 * pre-built Java class that represents a location on the screen based on
	 * it's (x,y) coordinate. There is also an overriden toString method which
	 * is helpful for displaying the value of the resolution objects. For
	 * example, an object that has been declared using: <i>new Resolution
	 * (1920,1080)</i>, will display "1920 x 1080" on the JComboBox.
	 * <p>
	 * <b> Class Fields: </b>
	 * <p>
	 * final long <b> serialVersionUID </b> - Used to prevent serializable class
	 * warnings.
	 */
	private static class Resolution extends Point {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor of the Resolution class. Calls the superclass constructor
		 * with the given values provided for <b> x </b> and <b> y</b>.
		 * <p>
		 * 
		 * @param x
		 *            - Stores the x-value (the width) of the screen size in
		 *            pixels.
		 * @param y
		 *            - Stores the y-value (the height) of the screen size in
		 *            pixels.
		 */
		public Resolution(int x, int y) {
			super(x, y);
		}

		/**
		 * The toString method in this class is used so that the screen
		 * dimensions can be appropriately displayed on the drop-down list in
		 * the desktop launcher's frame. For example, an object that has been
		 * declared using: <i>new Resolution (1920,1080)</i>, will display "1920
		 * x 1080" on the JComboBox.
		 */
		public String toString() {
			return "" + (int) this.getX() + " x " + (int) this.getY();
		}
	}

	/**
	 * The gameLauncher ( ) method sets up the JFrame for the desktop launcher.
	 * This method creates a 450 by 300 pixel JFrame that is centered on the
	 * screen. It has three drop-down menus and one start button on the frame.
	 * <p>
	 * <b> Class Fields: </b>
	 * <p>
	 * JButton <b> start </b> - Creates an instance of the JButton class called
	 * "Start Game".
	 * <p>
	 * Font <b> settingFonts </b> - Stores the instance of the custom font used
	 * for the drop-down menu labels.
	 * <p>
	 * JLabel <b> title </b> - Creates a JLabel that stores the title of the
	 * JFrame.
	 * <p>
	 * JLabel <b> resoLabel </b> - Creates a JLabel that stores the label of the
	 * resolution option.
	 * <p>
	 * JLabel <b> vSync</b> - Creates a JLabel that stores the label of the
	 * VSync option.
	 * <p>
	 * JLabel <b> fullScreenLabel </b> - Creates a JLabel that stores the label
	 * of the fullscreen option.
	 */
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

	/**
	 * This method provides a specific set of instructions to be executed if the
	 * listener interface detects that the "Start Game" button on the frame has
	 * been clicked. The set of instructions are to create the game client based
	 * on the initial configuration settings chosen by the user. Once the game
	 * is launched, the desktop launcher frame is disposed.
	 * 
	 * <p>
	 * <b> Class Fields: </b>
	 * <p>
	 * LwjglApplicationConfiguration <b> config </b> - Stores the game's
	 * configurations.
	 * <p>
	 * ProjectXploraGame <b> game </b> - Creates a new instance of the
	 * ProjectXploraGame.
	 * <p>
	 * @param e - Stores the event detected by the actionListener.
	 */
	public void actionPerformed(ActionEvent e) {
		width = (int) (((Resolution) (resolutionSelection.getSelectedItem())).getX());
		height = (int) (((Resolution) (resolutionSelection.getSelectedItem())).getY());
		fullscreen = fullscreenSelection.getSelectedItem().equals("Fullscreen");
		vsync = vSyncSelection.getSelectedItem().equals("ON");
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
		setup.dispose();
	}

	/**
	 * The main method creates the driver class for the application.
	 * <p>
	 * 
	 * @param args
	 *            - The arguments passed from the command line.
	 */
	public static void main(String[] args) {
		gameLauncher();
	}
}