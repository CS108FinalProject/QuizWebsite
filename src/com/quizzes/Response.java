package com.quizzes;

import java.util.List;

import com.util.Util;

/**
 * Constitutes a data structure for Response question.
 * @author Sam
 */
public class Response extends Question {

	private List<String> answers;
	
	public Response(String quizName, String question, List<String> answers) {
		super(quizName, question);
		
		Util.validateObject(answers);
		this.answers = answers;
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
}
