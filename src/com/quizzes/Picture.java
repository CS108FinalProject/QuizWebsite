package com.quizzes;

import java.util.Set;

import com.util.Util;

public class Picture extends Question {
	
	private String pictureUrl;
	private Set<String> answers;

	/**
	 * @param quizName
	 * @param question
	 * @param pictureUrl the url for the picture
	 * @param answers a list of possible answers to this question
	 */
	public Picture(String quizName, String question, String pictureUrl,
			Set<String> answers) {
		
		super(quizName, question);
		
		Util.validateString(pictureUrl);
		this.pictureUrl = pictureUrl;
		
		Util.validateObject(answers);
		this.answers = answers;
	}

	
	/**
	 * @return the pictureUrl
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}

	/**
	 * @param pictureUrl the pictureUrl to set
	 */
	public void setPictureUrl(String pictureUrl) {
		Util.validateString(pictureUrl);
		this.pictureUrl = pictureUrl;
	}

	/**
	 * @return the answers
	 */
	public Set<String> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(Set<String> answers) {
		Util.validateObject(answers);
		this.answers = answers;
	}
	
	
	/**
	 * Adds the passed answer to the question.
	 * @param answer
	 */
	public void addAnswer(String answer) {
		Util.validateString(answer);
		answers.add(answer);
	}
	
	
	/**
	 * Removes the passed answer from the question.
	 */
	public void removeAnswer(String answer) {
		Util.validateString(answer);
		if (!answers.contains(answer)) {
			throw new IllegalArgumentException(answer + " is not currently in this question.");
		}
		answers.remove(answer);
	}
	
	

}
