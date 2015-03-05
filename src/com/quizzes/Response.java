package com.quizzes;

import java.util.Set;

import com.util.Util;

/**
 * Constitutes a data structure for Response question.
 * @author Sam
 */
public class Response extends Question {

	private Set<String> answers;
	
	/**
	 * @param quizName the name of the quiz
	 * @param question the question or prompt in string form
	 * @param answers a list of valid answers
	 */
	public Response(String quizName, String question, Set<String> answers) {
		super(quizName, question);
		
		Util.validateObject(answers);
		this.answers = answers;
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
