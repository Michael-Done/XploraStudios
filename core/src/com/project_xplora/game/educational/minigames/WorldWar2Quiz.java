package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.project_xplora.game.Settings;

/**
 * The WorldWar2Quiz class feeds information to its superclass,
 * EducationalQuiz.java, which serves as the template for Project Xplorer's
 * educational mini game component.
 * <p>
 * Time taken to complete: 30 minutes.
 * 
 * <h2>Course Info:</h2> ICS4U0 with Krasteva, V.
 * <p>
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */

public class WorldWar2Quiz extends EducationalQuiz {

	/**
	 * WorldWar2Quiz Class Constructor. Initializes current settings, and adds
	 * the questions, choices, answers and sample image to the
	 * EducationalQuiz.java template class.
	 * 
	 * @param settings
	 *            - Stores the current settings.
	 */
	public WorldWar2Quiz(Settings currentSettings) {
		super(currentSettings);
		super.addElements("When did World War II begin?", "1939", "1945", "1960", "a");
		super.addElements("Who was the leader of Germany during 1939 to 1945?", "Adolf Hitler", "Benito Mussolini",
				"Joseph Stalin", "a");
		super.addElements("What event began World War II?", "Pearl Harbor", "Battle of Britain", "Invasion of Poland",
				"c");
		super.addElements("When did the bombing of Pearl Harbor happen?", "December 7th, 1941", "November 7th, 1941",
				"January 27th, 1945", "a");
		super.addElements("The main Axis powers of World War II consisted of?", "Germany, Russia, Japan",
				"Germany, Italy, Japan", "Germany, Italy, Romania", "b");
		super.addElements("Who was the Japanese admiral behind the Pearl Harbor attack?", "Myamoto", "Hirohito",
				"Yamamoto", "c");
		super.addElements("What country instigated conflict in North Africa?", "Germany ", "Italy ", "Britain", "a");
		super.addElements("Which European country did not participate in World War II?", "Spain", "France", "Germany",
				"a");
		super.addElements("When did D-Day happen?", "June 6th, 1944", "February 4th, 1944", "June 23rd, 1945", "a");
		super.addElements("Which country lost the most amount of people in the war?", "Germany", "Russia", "Canada",
				"b");
		super.addQuestionTexture(new Texture(Gdx.files.internal("MinigameLevel2Screen.jpg")));
	}
}
