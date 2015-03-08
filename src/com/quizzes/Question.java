package com.quizzes;

import java.util.Map;

import com.util.Util;

/**
 * Provides common functionality for Question objects.
 * @author Sam
 *
 */
public abstract class Question {
	
	protected String quizName;
	protected String question;
	
	/**
	 * @param quizName
	 * @param question
	 */
	public Question(String quizName, String question) {
		Util.validateString(quizName);
		Util.validateString(question);
		
		this.quizName = quizName;
		this.question = question;
	}
	

	/**
	 * @return the quizName
	 */
	public String getQuizName() {
		return quizName;
	}
	

	/**
	 * @param quizName the quizName to set
	 */
	public void setQuizName(String quizName) {
		Util.validateString(quizName);
		this.quizName = quizName;
	}
	

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		Util.validateString(question);
		this.question = question;
	}
	
	
	/**
	 * @return a representation of the object as a Map<String, Object>
	 */
	 public abstract Map<String, Object> toMap();
	 
}
