package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.project_xplora.game.Settings;

public class AncientRomeQuiz extends EducationalQuiz {

	public AncientRomeQuiz(Settings settings) {
		super(settings);
		super.addElements("Who was the most famous Emperor of Rome?", "Julius Caesar", "Marc Antony", "Caius Cassius", "a");
		super.addElements("What material did Ancient Romans primarily use to build their structures?", "Concrete", "Brick", "Limestone", "c");
		super.addElements("Gladiator fights were held where?", "The Colosseum", "The Pantheon", "Temple of Caesar", "a");
		super.addElements("What was The Pantheon?", "A palace", "A temple", "A prison", "b");
		super.addElements("What were aqueducts used for?", "Carrying water", "Decorations", "Fortifications", "a");
		super.addElements("Which country is Rome located in?", "Mexico", "Spain", "Italy", "c");
		super.addElements("What were poor citizens called in Ancient Rome?", "Plebians", "Peasants", "Bankrupt", "a");
		super.addElements("What were wealthy citizens called in Ancient Rome?", "Plebians", "Patricians", "Royalty", "b");
		super.addElements("Which author wrote a story based upon Ancient Rome?", "Aristotle", "Homer", "Shakespeare", "c");
		super.addElements("When did Julius Caesar die?", "March 15, 44 BC", "December 27th, 60 BC", "March 1st, 1 BC", "a");
		super.addQuestionTexture(new Texture(Gdx.files.internal("MinigameLevel3Screen.jpg")));
	}

}
