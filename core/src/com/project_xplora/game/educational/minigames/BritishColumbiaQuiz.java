package com.project_xplora.game.educational.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BritishColumbiaQuiz extends EducationalQuiz{

	private Texture[] questionGraphics, questionText, questionOptions, answers;
	private Texture quizScreen;
	private int questionIndex;
	private boolean[] questionsAsked;

	public BritishColumbiaQuiz() {
		questionsAsked = new boolean [9];
		quizScreen = new Texture(Gdx.files.internal("ChestUnlockScreen.jpg"));
		questionGraphics = new Texture [9];
		questionText = new Texture [9];
		questionOptions = new Texture [29];
		answers = new Texture [9];
	}

	@Override
	public void outputQuiz() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {

	}
}
