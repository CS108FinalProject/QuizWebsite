package com.quizzes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Constants;
import com.util.Util;

public class Picture extends Question implements Constants {
	
	private String pictureUrl;
	private List<String> answers;

	/**
	 * @param quizName
	 * @param question
	 * @param pictureUrl the url for the picture
	 * @param answers a list of possible answers to this question
	 */
	public Picture(String quizName, String question, String pictureUrl,
			List<String> answers) {
		
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
	public List<String> getAnswers() {
		return answers;
	}

	
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(List<String> answers) {
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
	
	
	/**
	 * Determines whether the passed answer is correct.
	 * @param answer provided by the test taker
	 * @return true if correct, false otherwise.
	 */
	public boolean answerIsCorrect(String answer) {
		Util.validateString(answer);
	
		if (answers.contains(answer)) return true;
		return false;
	}
	
	
	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(TYPE, PICTURE);
		result.put(QUIZ_NAME, quizName);
		result.put(QUESTION, question);
		result.put(PICTURE_URL, pictureUrl);
		result.put(ANSWERS, answers);
		return result;
	}

}
