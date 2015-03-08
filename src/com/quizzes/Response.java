package com.quizzes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Constants;
import com.util.Util;

/**
 * Constitutes a data structure for Response question.
 * @author Sam
 */
public class Response extends Question implements Constants {

	private List<String> answers;
	
	/**
	 * @param quizName the name of the quiz
	 * @param question the question or prompt in string form
	 * @param answers a list of valid answers
	 */
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
	
	
	/**
	 * Determines whether the passed answer is correct.
	 * @param answer provided by the user who is taking quiz.
	 * @return true if correct, false otherwise.
	 */
	public boolean answerIsCorrect(String answer) {
		Util.validateString(answer);
		if (answers.contains(answer)) return true;
		return false;
	}
	
	
	/**
	 * @return a representation of the object as a Map<String, Object>
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(QUIZ_NAME, quizName);
		result.put(QUESTION, question);
		result.put(ANSWERS, answers);
		return result;
	}
}
