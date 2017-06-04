package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.project_xplora.game.Settings;

public class AncientRomeQuiz extends EducationalQuiz {

	public AncientRomeQuiz(Settings settings) {
		super(settings);
		super.addElements("Who was the first Emperor of Rome?", "Julius Caesar", "Marc Antony", "Caius Cassius", "a");
		super.addElements("What material did Ancient Romans primarily use to build their structures?", "Concrete", "Brick", "Limestone", "c");
		super.addElements("", "", "", "", "");
		super.addElements("", "", "", "", "");
		super.addElements("", "", "", "", "");
		super.addElements("", "", "", "", "");
		super.addElements("", "", "", "", "");
		super.addElements("", "", "", "", "");
		super.addElements("", "", "", "", "");
		super.addElements("", "", "", "", "");
		super.addQuestionTexture(new Texture(Gdx.files.internal("MinigameLevel3Screen.jpg")));
	}

}
